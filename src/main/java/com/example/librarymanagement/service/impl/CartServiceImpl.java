package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.constant.BookCondition;
import com.example.librarymanagement.constant.ErrorMessage;
import com.example.librarymanagement.constant.SortByDataConstant;
import com.example.librarymanagement.constant.SuccessMessage;
import com.example.librarymanagement.domain.dto.common.CommonResponseDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanagement.domain.dto.pagination.PagingMeta;
import com.example.librarymanagement.domain.dto.response.cart.CartDetailResponseDto;
import com.example.librarymanagement.domain.dto.response.receipt.BorrowRequestSummaryResponseDto;
import com.example.librarymanagement.domain.entity.*;
import com.example.librarymanagement.domain.specification.CartSpecification;
import com.example.librarymanagement.exception.BadRequestException;
import com.example.librarymanagement.exception.NotFoundException;
import com.example.librarymanagement.repository.*;
import com.example.librarymanagement.service.CartService;
import com.example.librarymanagement.util.MessageUtil;
import com.example.librarymanagement.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    private final CartDetailRepository cartDetailRepository;

    private final ReaderRepository readerRepository;

    private final BookRepository bookRepository;
    private  final BookDefinitionRepository bookDefinitionRepository;
    private final MessageUtil messageUtil;

    @Value("${cartDetail.maxBooks:5}")
    private int maxBooksInCart;

    @Value("${cartDetail.borrowTimeHours:2}")
    private int borrowTimeHours;

    private Cart getEntity(String cardNumber) {
        return cartRepository.findByReaderCardNumber(cardNumber).orElseGet(() -> {
            Cart cart = new Cart();
            Reader reader = readerRepository.findByCardNumber(cardNumber)
                    .orElseThrow(() -> new NotFoundException(ErrorMessage.Reader.ERR_NOT_FOUND_CARD_NUMBER, cardNumber));
            cart.setReader(reader);
            return cartRepository.save(cart);
        });
    }
    @Override
    public List<CartDetailResponseDto> getCartDetails(String cardNumber, String title, String type) {
        Cart cart = getEntity(cardNumber);
        List<CartDetailResponseDto> responseDto = cartDetailRepository.getAllByCartId(cart.getId());

        if (title != null && !title.isEmpty()) {
            responseDto = responseDto.stream()
                    .filter(cartDetail -> cartDetail.getTitle().toLowerCase().contains(title.toLowerCase()))
                    .toList();
        }

        if (type != null) {
            LocalDateTime now = LocalDateTime.now();
            if (type.equals("1")) {
                responseDto = responseDto.stream()
                        .filter(cartDetail -> !cartDetail.getBorrowTo().isBefore(now))
                        .toList();
            } else if (type.equals("2")) {
                responseDto = responseDto.stream()
                        .filter(cartDetail -> cartDetail.getBorrowTo().isBefore(now))
                        .toList();
            }
        }

        return responseDto;
    }

 //   @Override
    public CommonResponseDto addToCart2(String cardNumber, Long bookId) {
        Cart cart = getEntity(cardNumber);

        //Kiem tra gio hang day chua
        if (cart.getCartDetails().size() >= maxBooksInCart) {
            throw new BadRequestException(ErrorMessage.Cart.ERR_MAX_BOOKS_IN_CART);
        }

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Book.ERR_NOT_FOUND_ID, bookId));

        CartDetail cartDetail = new CartDetail();
        cartDetail.setBook(book);
        cartDetail.setCart(cart);

        cartDetailRepository.save(cartDetail);

        String message = messageUtil.getMessage(SuccessMessage.CREATE);
        return new CommonResponseDto(message);
    }

    public CommonResponseDto addToCart(String cardNumber, Long bookId) {
        Cart cart = getEntity(cardNumber);
        LocalDateTime now = LocalDateTime.now();

        long count = cart.getCartDetails().stream()
                .filter(cartDetail -> cartDetail.getBorrowTo().isAfter(now))
                .count();

        if (count >= maxBooksInCart) {
            throw new BadRequestException(ErrorMessage.Cart.ERR_MAX_BOOKS_IN_CART);
        }

        BookDefinition bookDefinition = bookDefinitionRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.BookDefinition.ERR_NOT_FOUND_ID, bookId));
        Book book = null;

        for (Book bookCandidate : bookDefinition.getBooks()) {
            // Kiểm tra nếu sách có trong phiếu xuất
            if (bookCandidate.getExportReceipt() != null) {
                continue;
            }

            // Kiểm tra sách có rảnh không
            if (!bookCandidate.getBookCondition().equals(BookCondition.AVAILABLE)) {
                continue;
            }

            // Kiểm tra xem sách đã được giữ chỗ chưa
            List<CartDetail> cartDetails = bookCandidate.getCartDetails();
            boolean isBookReserved = cartDetails.stream().anyMatch(cartDetail -> cartDetail.getBorrowTo().isAfter(now));
            if (isBookReserved) {
                continue;
            }

            // Gán sách khả dụng cho biến `book` và thoát vòng lặp
            book = bookCandidate;
            break;
        }

        if (book == null) {
            throw new NotFoundException(ErrorMessage.BookDefinition.ERR_OUT_OF_STOCK);
        }

        CartDetail cartDetail = new CartDetail();
        cartDetail.setBook(book);
        cartDetail.setCart(cart);

        cartDetail.setBorrowFrom(now);
        cartDetail.setBorrowTo(now.plusHours(borrowTimeHours));

        cartDetailRepository.save(cartDetail);

        String message = messageUtil.getMessage(SuccessMessage.CREATE);
        return new CommonResponseDto(message);
    }

    @Override
    public CommonResponseDto removeFromCart(String cardNumber, Set<Long> cartDetailIds) {
        Cart cart = getEntity(cardNumber);

        cart.getCartDetails().removeIf(cartDetail -> cartDetailIds.contains(cartDetail.getId()));

        cartRepository.save(cart);

        String message = messageUtil.getMessage(SuccessMessage.DELETE);
        return new CommonResponseDto(message);
    }

    @Override
    public CommonResponseDto clearCart(String cardNumber) {
        Cart cart = getEntity(cardNumber);

        cart.getCartDetails().clear();

        cartRepository.save(cart);

        String message = messageUtil.getMessage(SuccessMessage.DELETE);
        return new CommonResponseDto(message);
    }

    @Override
    public PaginationResponseDto<BorrowRequestSummaryResponseDto> getPendingBorrowRequests(PaginationFullRequestDto requestDto) {
        Pageable pageable = PaginationUtil.buildPageable(requestDto, SortByDataConstant.BORROW_REQUEST);

        Page<Cart> page = cartRepository.findAll(
                CartSpecification.filterCarts(requestDto.getKeyword(), requestDto.getSearchBy()),
                pageable);

        LocalDateTime now = LocalDateTime.now();
        List<BorrowRequestSummaryResponseDto> items = new ArrayList<>();
        List<Cart> carts = page.getContent();
        for (Cart cart : carts) {
            List<CartDetail> cartDetails = cart.getCartDetails();
            List<CartDetail> filteredCartDetails = new ArrayList<>();
            for (CartDetail cartDetail : cartDetails) {
//                if (cartDetail.getBorrowTo().isAfter(now)) {
                    filteredCartDetails.add(cartDetail);

            }
            if (!filteredCartDetails.isEmpty()) {
                BorrowRequestSummaryResponseDto dto = new BorrowRequestSummaryResponseDto();
                dto.setCartDetails(filteredCartDetails);
                dto.setReader(cart.getReader());
                items.add(dto);
            }
        }

        PagingMeta pagingMeta = PaginationUtil.buildPagingMeta(requestDto, SortByDataConstant.BORROW_REQUEST, page);
        PaginationResponseDto<BorrowRequestSummaryResponseDto> responseDto = new PaginationResponseDto<>();

        responseDto.setItems(items);
        responseDto.setMeta(pagingMeta);

        return responseDto;
    }
}

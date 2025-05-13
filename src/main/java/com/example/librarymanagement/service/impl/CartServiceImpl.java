package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.constant.ErrorMessage;
import com.example.librarymanagement.constant.SuccessMessage;
import com.example.librarymanagement.domain.dto.common.CommonResponseDto;
import com.example.librarymanagement.domain.dto.response.cart.CartDetailResponseDto;
import com.example.librarymanagement.domain.entity.Book;
import com.example.librarymanagement.domain.entity.Cart;
import com.example.librarymanagement.domain.entity.CartDetail;
import com.example.librarymanagement.domain.entity.Reader;
import com.example.librarymanagement.exception.BadRequestException;
import com.example.librarymanagement.exception.NotFoundException;
import com.example.librarymanagement.repository.BookRepository;
import com.example.librarymanagement.repository.CartDetailRepository;
import com.example.librarymanagement.repository.CartRepository;
import com.example.librarymanagement.repository.ReaderRepository;
import com.example.librarymanagement.service.CartService;
import com.example.librarymanagement.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    private final CartDetailRepository cartDetailRepository;

    private final ReaderRepository readerRepository;

    private final BookRepository bookRepository;

    private final MessageUtil messageUtil;

    @Value("${cartDetail.maxBooks}")
    private int maxBooksInCart;

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
    public List<CartDetailResponseDto> getCartDetails(String cardNumber) {
        return cartDetailRepository.getAllByCardNumber(cardNumber);
    }

    @Override
    public CommonResponseDto addToCart(String cardNumber, String bookCode) {
        Cart cart = getEntity(cardNumber);

        //Kiem tra gio hang day chua
        if (cart.getCartDetails().size() >= maxBooksInCart) {
            throw new BadRequestException(ErrorMessage.Cart.ERR_MAX_BOOKS_IN_CART);
        }

        Book book = bookRepository.findByBookCode(bookCode)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Book.ERR_NOT_FOUND_CODE, bookCode));

        CartDetail cartDetail = new CartDetail();
        cartDetail.setBook(book);
        cartDetail.setCart(cart);

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
}

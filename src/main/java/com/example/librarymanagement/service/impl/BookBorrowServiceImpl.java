package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.constant.*;
import com.example.librarymanagement.domain.dto.common.CommonResponseDto;
import com.example.librarymanagement.domain.dto.filter.TimeFilter;
import com.example.librarymanagement.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanagement.domain.dto.pagination.PagingMeta;
import com.example.librarymanagement.domain.dto.request.book.BookReturnRequestDto;
import com.example.librarymanagement.domain.dto.response.bookborrow.BookBorrowResponseDto;
import com.example.librarymanagement.domain.entity.Book;
import com.example.librarymanagement.domain.entity.BookBorrow;
import com.example.librarymanagement.domain.entity.BorrowReceipt;
import com.example.librarymanagement.domain.specification.BookBorrowSpecification;
import com.example.librarymanagement.exception.BadRequestException;
import com.example.librarymanagement.exception.NotFoundException;
import com.example.librarymanagement.repository.BookBorrowRepository;
import com.example.librarymanagement.repository.BookRepository;
import com.example.librarymanagement.repository.BorrowReceiptRepository;
import com.example.librarymanagement.service.BookBorrowService;
import com.example.librarymanagement.service.BorrowReceiptService;
import com.example.librarymanagement.service.LogService;
import com.example.librarymanagement.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BookBorrowServiceImpl implements BookBorrowService {

    private static final String TAG = "Quản lý phiếu mượn";

    private final MessageSource messageSource;

    private final LogService logService;

    private final BookRepository bookRepository;

    private final BookBorrowRepository bookBorrowRepository;

    private final BorrowReceiptRepository borrowReceiptRepository;

    private final BorrowReceiptService borrowReceiptService;

    private BookBorrow getEntity(Long id) {
        return bookBorrowRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.BookBorrow.ERR_NOT_FOUND_ID, id));
    }

    @Override
    public PaginationResponseDto<BookBorrowResponseDto> findAll(PaginationFullRequestDto requestDto, TimeFilter timeFilter, List<BookBorrowStatus> status) {
        Pageable pageable = PaginationUtil.buildPageable(requestDto, SortByDataConstant.BOOK_BORROW);

        Specification<BookBorrow> specification =
                BookBorrowSpecification.filterBookBorrows(status)
                        .and(BookBorrowSpecification.filterBookBorrows(timeFilter))
                        .and(BookBorrowSpecification.filterBookBorrows(requestDto.getKeyword(),requestDto.getSearchBy()));

        Page<BookBorrow> page = bookBorrowRepository.findAll(specification, pageable);

        List<BookBorrowResponseDto> items = page.getContent().stream()
                .map(BookBorrowResponseDto::new)
                .toList();

        PagingMeta pagingMeta = PaginationUtil.buildPagingMeta(requestDto, SortByDataConstant.BOOK_BORROW, page);

        PaginationResponseDto<BookBorrowResponseDto> responseDto = new PaginationResponseDto<>();
        responseDto.setItems(items);
        responseDto.setMeta(pagingMeta);

        return responseDto;
    }

    @Override
    @Transactional
    public CommonResponseDto returnBooks(List<BookReturnRequestDto> requestDtos, String userId) {
        boolean isReturned = false;
        StringBuilder logMessage = new StringBuilder();

        for (BookReturnRequestDto requestDto : requestDtos) {
            BookBorrow bookBorrow = getEntity(requestDto.getBookBorrowId());
            if (!bookBorrow.getStatus().equals(BookBorrowStatus.NOT_RETURNED)){
                continue;
            }

            bookBorrow.setReturnDate(LocalDate.now());
            bookBorrow.setStatus(BookBorrowStatus.RETURNED);

            Book book = bookBorrow.getBook();
            book.setBookCondition(BookCondition.AVAILABLE);
            if (requestDto.getBookStatus() != null) {
                book.setBookStatus(requestDto.getBookStatus());
            }

            BorrowReceipt borrowReceipt = bookBorrow.getBorrowReceipt();
            borrowReceiptService.updateBorrowStatus(borrowReceipt);

            logMessage.append(book.getBookCode()).append(", ");

            bookRepository.save(book);
            bookBorrowRepository.save(bookBorrow);
            borrowReceiptRepository.save(borrowReceipt);

            isReturned = true;
        }

        if (!isReturned) {
            throw new BadRequestException(ErrorMessage.BookBorrow.ERR_NOT_RETURNED_IN_ANOTHER_RECEIPT);
        }
        if (logMessage.length() > 2) {
            logMessage.setLength(logMessage.length() - 2);
        }
        logService.createLog(TAG, EventConstants.EDIT, "Trả sách mã: " + logMessage, userId);

        String message = messageSource.getMessage(SuccessMessage.UPDATE,null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    @Override
    @Transactional
    public CommonResponseDto reportLostBookByIds(Set<Long> ids, String userId) {
        boolean isLostReported = false;
        StringBuilder logMessage = new StringBuilder();

        for (Long id : ids) {
            BookBorrow bookBorrow = getEntity(id);
            if (!bookBorrow.getStatus().equals(BookBorrowStatus.NOT_RETURNED)) {
                continue;
            }
            bookBorrow.setStatus(BookBorrowStatus.LOST);

            Book book = bookBorrow.getBook();
            book.setBookCondition(BookCondition.LOST);

            BorrowReceipt borrowReceipt = bookBorrow.getBorrowReceipt();
            borrowReceiptService.updateBorrowStatus(borrowReceipt);

            logMessage.append(book.getBookCode()).append(", ");

            bookRepository.save(book);
            bookBorrowRepository.save(bookBorrow);
            borrowReceiptRepository.save(borrowReceipt);

            isLostReported = true;
        }

        if (!isLostReported) {
            throw new BadRequestException(ErrorMessage.BookBorrow.ERR_NOT_FOUND_IDS);
        }
        if (logMessage.length() > 2) {
            logMessage.setLength(logMessage.length() - 2);
        }

        logService.createLog(TAG, EventConstants.EDIT, "Báo mất sách với mã: " + logMessage, userId);

        String message = messageSource.getMessage(SuccessMessage.UPDATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }
}

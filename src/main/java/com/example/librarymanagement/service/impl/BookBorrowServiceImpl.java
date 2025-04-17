package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.constant.BookBorrowStatus;
import com.example.librarymanagement.constant.ErrorMessage;
import com.example.librarymanagement.constant.SortByDataConstant;
import com.example.librarymanagement.domain.dto.common.CommonResponseDto;
import com.example.librarymanagement.domain.dto.filter.TimeFilter;
import com.example.librarymanagement.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanagement.domain.dto.pagination.PaginationResponseDto;
import com.example.librarymanagement.domain.dto.request.book.BookReturnRequestDto;
import com.example.librarymanagement.domain.dto.response.bookborrow.BookBorrowResponseDto;
import com.example.librarymanagement.domain.entity.BookBorrow;
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
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

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


        return null;
    }

    @Override
    public CommonResponseDto returnBooks(List<BookReturnRequestDto> requestDtos, String userId) {
        return null;
    }

    @Override
    public CommonResponseDto reportLostBookByIds(Set<Long> ids, String userId) {
        return null;
    }
}

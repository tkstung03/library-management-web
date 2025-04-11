package com.example.librarymanagement.domain.dto.response.receipt;

import com.example.librarymanagement.constant.BorrowStatus;
import com.example.librarymanagement.domain.dto.response.bookborrow.BookBorrowDto;
import com.example.librarymanagement.domain.entity.BorrowReceipt;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class BorrowReceiptDetailDto {

    private final long id;

    private final String receiptNumber;

    private final String fullName;

    private final LocalDate borrowDate;

    private final LocalDate dueDate;

    private final BorrowStatus status;

    private final List<BookBorrowDto> books;

    public BorrowReceiptDetailDto(BorrowReceipt borrowReceipt){
        this.id = borrowReceipt.getId();
        this.receiptNumber = borrowReceipt.getReceiptNumber();
        this.fullName = borrowReceipt.getReader().getFullName();
        this.borrowDate = borrowReceipt.getBorrowDate();
        this.dueDate = borrowReceipt.getDueDate();
        this.status = borrowReceipt.getStatus();
        this.books = borrowReceipt.getBookBorrows().stream()
                .map(BookBorrowDto::new)
                .toList();
    }
}

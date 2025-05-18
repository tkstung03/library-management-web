package com.example.librarymanagement.domain.dto.response.bookborrow;

import com.example.librarymanagement.constant.BookBorrowStatus;
import com.example.librarymanagement.domain.entity.BookBorrow;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class BookBorrowDto {

    private final String title;
    private final String bookCode;
    private final LocalDate returnDate;
    private final BookBorrowStatus status;

    public BookBorrowDto(BookBorrow bookBorrow){
        this.title = bookBorrow.getBook().getBookDefinition().getTitle();
        this.bookCode = bookBorrow.getBook().getBookCode();
        this.returnDate = bookBorrow.getReturnDate();
        this.status = bookBorrow.getStatus();
    }
}

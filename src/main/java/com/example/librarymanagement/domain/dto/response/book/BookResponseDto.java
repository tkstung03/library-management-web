package com.example.librarymanagement.domain.dto.response.book;

import com.example.librarymanagement.constant.BookCondition;
import com.example.librarymanagement.constant.BookStatus;
import com.example.librarymanagement.domain.dto.request.book.BookDefinitionRequestDto;
import com.example.librarymanagement.domain.dto.response.bookdefinition.BookDefinitionResponseDto;
import com.example.librarymanagement.domain.entity.Book;
import lombok.Getter;

@Getter
public class BookResponseDto {

    private final Long id;

    private final String bookCode;

    private final BookCondition bookCondition;

    private final BookStatus bookStatus;

    private final BookDefinitionResponseDto bookDefinition;

    public BookResponseDto(Book book){

        this.id = book.getId();
        this.bookCode = book.getBookCode();
        this.bookCondition = book.getBookCondition();
        this.bookStatus = book.getBookStatus();
        this.bookDefinition = new BookDefinitionResponseDto(book.getBookDefinition());

    }
}

package com.example.librarymanagement.domain.dto.response.bookdefinition;

import com.example.librarymanagement.domain.dto.common.BaseEntityDto;
import com.example.librarymanagement.domain.entity.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BookByBookDefinitionResponseDto {

    private final long id;

    private final String title;

    private final String bookNumber;

    private final Integer publishingYear;

    private final BaseEntityDto classificationSymbol;

    private final List<BaseEntityDto> authors = new ArrayList<>();

    private final BaseEntityDto publisher;

    private long totalBooks;

    private long availableBooks;

    private long borrowedBooks;

    private long lostBooks;

    public BookByBookDefinitionResponseDto(BookDefinition bookDefinition){
        this.id = bookDefinition.getId();
        this.title = bookDefinition.getTitle();
        this.bookNumber = bookDefinition.getBookNumber();
        this.publishingYear = bookDefinition.getPublishingYear();
        this.totalBooks = 0;
        this.availableBooks = 0;
        this.borrowedBooks = 0;
        this.lostBooks = 0;

        for (Book book : bookDefinition.getBooks()){
            if (book.getExportReceipt() != null){
                continue;
            }
            this.totalBooks++;
            switch (book.getBookCondition()){
                case AVAILABLE:
                    this.availableBooks++;
                    break;
                case ON_LOAN:
                    this.borrowedBooks++;
                    break;
                case LOST:
                    this.lostBooks++;
                    break;
            }
        }
        // Set authors
        List<BookAuthor> au = bookDefinition.getBookAuthors();
        if (au != null) {
            this.authors.addAll(au.stream()
                    .map(BookAuthor::getAuthor)
                    .map(author -> new BaseEntityDto(author.getId(), author.getFullName()))
                    .toList());
        }

        // Set publisher
        Publisher p = bookDefinition.getPublisher();
        this.publisher = p != null ? new BaseEntityDto(p.getId(), p.getName()) : null;

        //Set classificationSymbol
        ClassificationSymbol c = bookDefinition.getClassificationSymbol();
        this.classificationSymbol = c != null ? new BaseEntityDto(c.getId(), c.getCode()) : null;
    }
}

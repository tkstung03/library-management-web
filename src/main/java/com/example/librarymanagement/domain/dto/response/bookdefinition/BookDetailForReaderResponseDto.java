package com.example.librarymanagement.domain.dto.response.bookdefinition;

import com.example.librarymanagement.constant.BookCondition;
import com.example.librarymanagement.domain.dto.common.BaseEntityDto;
import com.example.librarymanagement.domain.entity.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class BookDetailForReaderResponseDto {

    private final long id;

    private final String title;

    private final String isbn;

    private final Integer pageCount;

    private final long bookCount;

    private final String bookSize;

    private final String language;

    private final String imageUrl;

    private final String summary;

    private final Integer publishingYear;

    private final BaseEntityDto publisher;

    private final BaseEntityDto bookSet;

    private final BaseEntityDto category;

    private final List<BaseEntityDto> authors = new ArrayList<>();

    public BookDetailForReaderResponseDto(BookDefinition bookDefinition){

        this.id = bookDefinition.getId();
        this.title = bookDefinition.getTitle();
        this.isbn = bookDefinition.getIsbn();
        this.pageCount = bookDefinition.getPageCount();
        this.bookSize = bookDefinition.getBookSize();
        this.language = bookDefinition.getLanguage();
        this.imageUrl = bookDefinition.getImageUrl();
        this.summary = bookDefinition.getSummary();
        this.publishingYear = bookDefinition.getPublishingYear();

        //bookCount
        this.bookCount = bookDefinition.getBooks().stream()
                .filter(book -> book.getBookCondition() == BookCondition.AVAIABLE && book.getExportReceipt() == null)
                .count();

        // set category
        Category c =bookDefinition.getCategory();
        this.category = c != null ? new BaseEntityDto(c.getId(), c.getCategoryName()) :null;

        // Set publisher
        Publisher p = bookDefinition.getPublisher();
        this.publisher = p != null ? new BaseEntityDto(p.getId(), p.getName()) : null;

        // Set bookSet
        BookSet b = bookDefinition.getBookSet();
        this.bookSet = b != null ? new BaseEntityDto(b.getId(), b.getName()) : null;

        // set author
        List<BookAuthor> au = bookDefinition.getBookAuthors();
        if(au != null){
            this.authors.addAll(au.stream()
                    .map(BookAuthor::getAuthor)
                    .map(author -> new BaseEntityDto(author.getId(), author.getFullName()))
                    .toList());
        }
    }
}

package com.example.librarymanagement.domain.dto.response.cart;

import com.example.librarymanagement.domain.dto.common.BaseEntityDto;
import com.example.librarymanagement.domain.entity.Book;
import com.example.librarymanagement.domain.entity.BookAuthor;
import com.example.librarymanagement.domain.entity.CartDetail;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class CartDetailResponseDto {
    private final long id;

    private final String bookCode;

    private final String title;

    private final List<BaseEntityDto> authors =new ArrayList<>();

    public CartDetailResponseDto(CartDetail cartDetail){
        this.id = cartDetail.getId();
        this.bookCode = cartDetail.getBook().getBookCode();
        this.title = cartDetail.getBook().getBookDefinition().getTitle();

        //set authors
        List<BookAuthor> au = cartDetail.getBook().getBookDefinition().getBookAuthors();

        if(au != null){
            this.authors.addAll(au.stream()
                    .map(BookAuthor::getAuthor)
                    .map(author -> new BaseEntityDto(author.getId(), author.getFullName()))
                    .toList());
        }
    }
}

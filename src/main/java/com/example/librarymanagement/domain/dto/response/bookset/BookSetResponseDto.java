package com.example.librarymanagement.domain.dto.response.bookset;

import com.example.librarymanagement.domain.entity.BookSet;
import lombok.Getter;

@Getter
public class BookSetResponseDto {

    private final long id;

    private final String name;

    private final Boolean activeFlag;

    private final int bookCount;

    public BookSetResponseDto(BookSet bookSet){
        this.id = bookSet.getId();
        this.name = bookSet.getName();
        this.activeFlag = bookSet.getActiveFlag();
        this.bookCount = bookSet.getBookDefinitions() == null ? 0 : bookSet.getBookDefinitions().size();
    }
}

package com.example.librarymanagement.domain.mapper;

import com.example.librarymanagement.domain.dto.request.book.BookSetRequestDto;
import com.example.librarymanagement.domain.entity.BookSet;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookSetMapper {
    BookSet toBookSet(BookSetRequestDto requestDto);
}

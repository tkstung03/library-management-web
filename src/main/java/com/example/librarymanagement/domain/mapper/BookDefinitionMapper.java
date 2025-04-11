package com.example.librarymanagement.domain.mapper;

import com.example.librarymanagement.domain.dto.request.book.BookDefinitionRequestDto;
import com.example.librarymanagement.domain.entity.BookDefinition;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookDefinitionMapper {
    BookDefinition toBookDefinition(BookDefinitionRequestDto requestDto);
}

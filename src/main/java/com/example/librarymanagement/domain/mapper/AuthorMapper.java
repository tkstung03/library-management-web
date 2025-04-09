package com.example.librarymanagement.domain.mapper;

import com.example.librarymanagement.domain.dto.request.author.AuthorRequestDto;
import com.example.librarymanagement.domain.entity.Author;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthorMapper {

    Author toAuthor(AuthorRequestDto requestDto);
}

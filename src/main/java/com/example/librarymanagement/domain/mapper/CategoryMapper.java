package com.example.librarymanagement.domain.mapper;

import com.example.librarymanagement.domain.dto.request.category.CategoryRequestDto;
import com.example.librarymanagement.domain.entity.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toCategory (CategoryRequestDto requestDto);
}

package com.example.librarymanagement.domain.mapper;

import com.example.librarymanagement.domain.dto.request.category.CategoryGroupRequestDto;
import com.example.librarymanagement.domain.entity.CategoryGroup;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryGroupMapper {

    CategoryGroup toCategoryGroup(CategoryGroupRequestDto requestDto);

}

package com.example.librarymanagement.domain.mapper;

import com.example.librarymanagement.domain.dto.request.other.ClassificationSymbolRequestDto;
import com.example.librarymanagement.domain.entity.ClassificationSymbol;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClassificationSymbolMapper {
    ClassificationSymbol toClassificationSymbol (ClassificationSymbolRequestDto requestDto);
}

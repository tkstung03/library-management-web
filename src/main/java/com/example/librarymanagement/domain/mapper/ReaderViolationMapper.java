package com.example.librarymanagement.domain.mapper;

import com.example.librarymanagement.domain.dto.request.reader.ReaderViolationRequestDto;
import com.example.librarymanagement.domain.entity.ReaderViolation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReaderViolationMapper {
    ReaderViolation toReaderViolation(ReaderViolationRequestDto requestDto);
}

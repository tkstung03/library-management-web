package com.example.librarymanagement.domain.mapper;

import com.example.librarymanagement.domain.dto.request.reader.ReaderRequestDto;
import com.example.librarymanagement.domain.entity.Reader;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReaderMapper {
    Reader toReader(ReaderRequestDto requestDto);

}

package com.example.librarymanagement.domain.mapper;

import com.example.librarymanagement.domain.dto.request.major.MajorRequestDto;
import com.example.librarymanagement.domain.entity.Major;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MajorMapper {
    Major toMajor(MajorRequestDto requestDto);
}
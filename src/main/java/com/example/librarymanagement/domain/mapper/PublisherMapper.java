package com.example.librarymanagement.domain.mapper;

import com.example.librarymanagement.domain.dto.request.publisher.PublisherRequestDto;
import com.example.librarymanagement.domain.entity.Publisher;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PublisherMapper {
    Publisher toPublisher(PublisherRequestDto requestDto);
}

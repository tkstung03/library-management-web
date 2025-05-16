package com.example.librarymanagement.domain.mapper;

import com.example.librarymanagement.domain.dto.request.auth.RegisterRequestDto;
import com.example.librarymanagement.domain.dto.request.user.UserRequestDto;
import com.example.librarymanagement.domain.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(RegisterRequestDto requestDto);

    @Mapping(source = "status", target = "status")
    User toUser(UserRequestDto requestDto);
}

package com.example.librarymanagement.domain.mapper;

import com.example.librarymanagement.domain.dto.request.auth.RegisterRequestDto;
import com.example.librarymanagement.domain.dto.request.user.UserRequestDto;
import com.example.librarymanagement.domain.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(RegisterRequestDto requestDto);
    User toUser(UserRequestDto requestDto);
}

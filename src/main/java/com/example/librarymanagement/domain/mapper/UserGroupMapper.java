package com.example.librarymanagement.domain.mapper;

import com.example.librarymanagement.domain.dto.request.user.UserGroupRequestDto;
import com.example.librarymanagement.domain.entity.UserGroup;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserGroupMapper {
    UserGroup toUserGroup(UserGroupRequestDto requestDto);
}

package com.example.librarymanagement.domain.dto.response.user;

import com.example.librarymanagement.constant.RoleConstant;
import com.example.librarymanagement.domain.entity.Role;
import lombok.Getter;

@Getter
public class RoleResponseDto {

    private final byte id;

    private final String name;

    private final RoleConstant code;

    public RoleResponseDto(Role role) {
        this.id = role.getId();
        this.name = role.getName();
        this.code = role.getCode();
    }
}

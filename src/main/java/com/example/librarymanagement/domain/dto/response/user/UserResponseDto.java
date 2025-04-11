package com.example.librarymanagement.domain.dto.response.user;

import com.example.librarymanagement.constant.AccountStatus;
import com.example.librarymanagement.domain.dto.common.BaseEntityDto;
import com.example.librarymanagement.domain.entity.User;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class UserResponseDto {

    private final String id;

    private final String address;

    private final String email;

    private final LocalDate expiryDate;

    private final String fullName;

    private final String note;

    private final String username;

    private final String phoneNumber;

    private final String position;

    private AccountStatus status;

    private BaseEntityDto userGroup;

    public UserResponseDto(User user){
        this.id = user.getId();
        this.address = user.getAddress();
        this.email = user.getEmail();
        this.expiryDate = user.getExpiryDate();
        this.fullName = user.getFullName();
        this.note = user.getNote();
        this.username = user.getUsername();
        this.position = user.getPosition();
        this.phoneNumber = user.getPhoneNumber();
        this.status = user.getStatus();
        this.userGroup =new BaseEntityDto(user.getUserGroup().getId(), user.getUserGroup().getName());
    }
}

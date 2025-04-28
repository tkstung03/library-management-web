package com.example.librarymanagement.security;

import com.example.librarymanagement.domain.entity.Reader;
import com.example.librarymanagement.domain.entity.User;

public class UserDetailsFactory {
    public static CustomUserDetails fromUser(User user) {
        return CustomUserDetails.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .expiryDate(user.getExpiryDate())
                .accountStatus(user.getStatus())
                .authorities(AuthorityBuilder.fromUser(user))
                .build();
    }

    public static CustomUserDetails fromReader(Reader reader) {
        return CustomUserDetails.builder()
                .cardNumber(reader.getCardNumber())
                .username(reader.getFullName())
                .password(reader.getPassword())
                .expiryDate(reader.getExpiryDate())
                .cardStatus(reader.getStatus())
                .authorities(AuthorityBuilder.fromReader())
                .build();
    }
}

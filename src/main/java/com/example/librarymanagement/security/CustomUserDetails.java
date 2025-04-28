package com.example.librarymanagement.security;

import com.example.librarymanagement.constant.AccountStatus;
import com.example.librarymanagement.constant.CardStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;


@Builder
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

    @Getter
    private final String userId;

    @Getter
    private final String cardNumber;

    @Getter
    private final LocalDate expiryDate;

    @Getter
    private final AccountStatus accountStatus;

    @Getter
    private final CardStatus cardStatus;

    @JsonIgnore
    private final String username;

    @JsonIgnore
    private final String password;

    private final Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

}

package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.domain.dto.common.CommonResponseDto;
import com.example.librarymanagement.domain.dto.request.auth.*;
import com.example.librarymanagement.domain.dto.response.auth.CurrentUserLoginResponseDto;
import com.example.librarymanagement.domain.dto.response.auth.LoginResponseDto;
import com.example.librarymanagement.domain.dto.response.auth.TokenRefreshResponseDto;
import com.example.librarymanagement.security.CustomUserDetails;
import com.example.librarymanagement.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthServiceImpl implements AuthService {
    @Override
    public LoginResponseDto readerLogin(ReaderLoginRequestDto request) {
        return null;
    }

    @Override
    public LoginResponseDto adminLogin(AdminLoginRequestDto request) {
        return null;
    }

    @Override
    public CommonResponseDto logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        return null;
    }

    @Override
    public TokenRefreshResponseDto refresh(TokenRefreshRequestDto request) {
        return null;
    }

    @Override
    public CommonResponseDto adminForgetPassword(AdminForgetPasswordRequestDto requestDto) {
        return null;
    }

    @Override
    public CommonResponseDto adminChangePassword(ChangePasswordRequestDto requestDto, String username) {
        return null;
    }

    @Override
    public CommonResponseDto forgetPassword(ReaderForgetPasswordRequestDto requestDto) {
        return null;
    }

    @Override
    public CommonResponseDto changePassword(ChangePasswordRequestDto requestDto, String cardNumber) {
        return null;
    }

    @Override
    public CurrentUserLoginResponseDto getCurrentUser(CustomUserDetails userDetails) {
        return null;
    }
}

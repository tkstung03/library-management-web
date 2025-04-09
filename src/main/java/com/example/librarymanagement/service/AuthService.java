package com.example.librarymanagement.service;

import com.example.librarymanagement.domain.dto.common.CommonResponseDto;
import com.example.librarymanagement.domain.dto.request.auth.*;
import com.example.librarymanagement.domain.dto.response.auth.CurrentUserLoginResponseDto;
import com.example.librarymanagement.domain.dto.response.auth.LoginResponseDto;
import com.example.librarymanagement.domain.dto.response.auth.TokenRefreshResponseDto;
import com.example.librarymanagement.security.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;


public interface AuthService {

    LoginResponseDto readerLogin(ReaderLoginRequestDto request);

    LoginResponseDto adminLogin(AdminLoginRequestDto request);

    CommonResponseDto logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication);

    TokenRefreshResponseDto refresh(TokenRefreshRequestDto request);

    CommonResponseDto adminForgetPassword(AdminForgetPasswordRequestDto requestDto);

    CommonResponseDto adminChangePassword(ChangePasswordRequestDto requestDto, String username);

    CommonResponseDto forgetPassword(ReaderForgetPasswordRequestDto requestDto);

    CommonResponseDto changePassword(ChangePasswordRequestDto requestDto, String cardNumber);

    CurrentUserLoginResponseDto getCurrentUser(CustomUserDetails userDetails);


}

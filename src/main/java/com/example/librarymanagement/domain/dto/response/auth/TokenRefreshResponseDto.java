package com.example.librarymanagement.domain.dto.response.auth;

import com.example.librarymanagement.constant.CommonConstant;

public class TokenRefreshResponseDto {

    private final String tokenType = CommonConstant.TOKEN_TYPE;

    private final String accessToken;

    private final String refreshToken;

    public TokenRefreshResponseDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}

package com.example.librarymanagement.controller;

import com.example.librarymanagement.annotation.CurrentUser;
import com.example.librarymanagement.annotation.RestApiV1;
import com.example.librarymanagement.base.VsResponseUtil;
import com.example.librarymanagement.constant.UrlConstant;
import com.example.librarymanagement.domain.dto.request.auth.*;
import com.example.librarymanagement.security.CustomUserDetails;
import com.example.librarymanagement.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestApiV1
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Auth")
public class AuthController {

    AuthService authService;

    @Operation(summary = "API Login")
    @PostMapping(UrlConstant.Auth.LOGIN)
    public ResponseEntity<?> login(@Valid @RequestBody ReaderLoginRequestDto request) {
        return VsResponseUtil.success(authService.readerLogin(request));
    }

    @Operation(summary = "API Admin Login")
    @PostMapping(UrlConstant.Auth.ADMIN_LOGIN)
    public ResponseEntity<?> login(@Valid @RequestBody AdminLoginRequestDto request) {
        return VsResponseUtil.success(authService.adminLogin(request));
    }

    @Operation(summary = "API Logout")
    @PostMapping(UrlConstant.Auth.LOGOUT)
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        return VsResponseUtil.success(authService.logout(request, response, authentication));
    }

    @Operation(summary = "API Refresh token")
    @PostMapping(UrlConstant.Auth.REFRESH_TOKEN)
    public ResponseEntity<?> refresh(@Valid @RequestBody TokenRefreshRequestDto request) {
        return VsResponseUtil.success(authService.refresh(request));
    }

    @Operation(summary = "API Admin forget password")
    @PostMapping(UrlConstant.Auth.ADMIN_FORGET_PASSWORD)
    public ResponseEntity<?> adminForgetPassword(@Valid @RequestBody AdminForgetPasswordRequestDto request) {
        return VsResponseUtil.success(authService.adminForgetPassword(request));
    }

    @Operation(summary = "API Admin change password")
    @PatchMapping(UrlConstant.Auth.ADMIN_CHANGE_PASSWORD)
    public ResponseEntity<?> adminChangePassword(@Valid @RequestBody ChangePasswordRequestDto request,
                                                 @CurrentUser CustomUserDetails userDetails) {
        return VsResponseUtil.success(authService.adminChangePassword(request, userDetails.getUsername()));
    }

    @Operation(summary = "API Forget password")
    @PostMapping(UrlConstant.Auth.FORGET_PASSWORD)
    public ResponseEntity<?> forgetPassword(@Valid @RequestBody ReaderForgetPasswordRequestDto request) {
        return VsResponseUtil.success(authService.forgetPassword(request));
    }

    @Operation(summary = "API Change password")
    @PatchMapping(UrlConstant.Auth.CHANGE_PASSWORD)
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequestDto request,
                                            @CurrentUser CustomUserDetails userDetails) {
        return VsResponseUtil.success(authService.changePassword(request, userDetails.getCardNumber()));
    }

    @Operation(summary = "API Get current user login")
    @GetMapping(UrlConstant.Auth.GET_CURRENT_USER)
    public ResponseEntity<?> getCurrentUser(@CurrentUser CustomUserDetails userDetails) {
        return VsResponseUtil.success(authService.getCurrentUser(userDetails));
    }


}

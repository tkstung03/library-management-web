package com.example.librarymanagement.controller;

import com.example.librarymanagement.annotation.CurrentUser;
import com.example.librarymanagement.annotation.RestApiV1;
import com.example.librarymanagement.base.VsResponseUtil;
import com.example.librarymanagement.constant.UrlConstant;
import com.example.librarymanagement.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanagement.domain.dto.request.user.UserRequestDto;
import com.example.librarymanagement.security.CustomUserDetails;
import com.example.librarymanagement.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestApiV1
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "User")
public class UserController {
    UserService userService;

    @Operation(summary = "API Create New User")
    @PreAuthorize("hasRole('ROLE_MANAGE_USER')")
    @PostMapping(UrlConstant.User.CREATE)
    public ResponseEntity<?> createUser(
            @Valid @RequestBody UserRequestDto requestDto,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(HttpStatus.CREATED, userService.save(requestDto, userDetails.getUserId()));
    }

    @Operation(summary = "API Update User")
    @PreAuthorize("hasRole('ROLE_MANAGE_USER')")
    @PutMapping(UrlConstant.User.UPDATE)
    public ResponseEntity<?> updateUser(
            @PathVariable String id,
            @Valid @RequestBody UserRequestDto requestDto,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(userService.update(id, requestDto, userDetails.getUserId()));
    }

    @Operation(summary = "API Delete User")
    @PreAuthorize("hasRole('ROLE_MANAGE_USER')")
    @DeleteMapping(UrlConstant.User.DELETE)
    public ResponseEntity<?> deleteUser(
            @PathVariable String id,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(userService.delete(id, userDetails.getUserId()));
    }

    @Operation(summary = "API Get All Users")
    @PreAuthorize("hasRole('ROLE_MANAGE_USER')")
    @GetMapping(UrlConstant.User.GET_ALL)
    public ResponseEntity<?> getAllUsers(@ParameterObject PaginationFullRequestDto requestDto) {
        return VsResponseUtil.success(userService.findAll(requestDto));
    }

    @Operation(summary = "API Get User By Id")
    @PreAuthorize("hasRole('ROLE_MANAGE_USER')")
    @GetMapping(UrlConstant.User.GET_BY_ID)
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        return VsResponseUtil.success(userService.findById(id));
    }

    @Operation(summary = "API upload images")
    @PreAuthorize("!hasRole('ROLE_READER')")
    @PostMapping(value = UrlConstant.User.UPLOAD_IMAGE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages(
            @RequestParam("files") List<MultipartFile> files,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(userService.uploadImages(files, userDetails.getUserId()));
    }
}

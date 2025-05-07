package com.example.librarymanagement.controller;

import com.example.librarymanagement.annotation.CurrentUser;
import com.example.librarymanagement.annotation.RestApiV1;
import com.example.librarymanagement.base.VsResponseUtil;
import com.example.librarymanagement.constant.UrlConstant;
import com.example.librarymanagement.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanagement.domain.dto.request.author.AuthorRequestDto;
import com.example.librarymanagement.security.CustomUserDetails;
import com.example.librarymanagement.service.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestApiV1
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Author")
public class AuthorController {
    AuthorService authorService;

    @Operation(summary = "API Create Author")
    @PreAuthorize("hasRole('ROLE_MANAGE_AUTHOR')")
    @PostMapping(UrlConstant.Author.CREATE)
    public ResponseEntity<?> createAuthor(@Valid @RequestBody AuthorRequestDto request,
                                          @CurrentUser CustomUserDetails userDetails) {
        return VsResponseUtil.success(HttpStatus.CREATED, authorService.save(request, userDetails.getUserId()));
    }

    @Operation(summary = "API Update Author")
    @PreAuthorize("hasRole('ROLE_MANAGE_AUTHOR')")
    @PutMapping(UrlConstant.Author.UPDATE)
    public ResponseEntity<?> updateAuthor(@PathVariable Long id,
                                          @Valid @RequestBody AuthorRequestDto request,
                                          @CurrentUser CustomUserDetails userDetails) {
        return VsResponseUtil.success(authorService.update(id, request, userDetails.getUserId()));
    }

    @Operation(summary = "API Delete Author")
    @PreAuthorize("hasRole('ROLE_MANAGE_ROLE')")
    @DeleteMapping(UrlConstant.Author.DELETE)
    public ResponseEntity<?> deleteAuthor(@PathVariable Long id,
                                          @CurrentUser CustomUserDetails userDetails) {
        return VsResponseUtil.success(authorService.delete(id, userDetails.getUserId()));
    }

    @Operation(summary = "API Get Authors")
    @PreAuthorize("hasAnyRole('ROLE_MANAGE_AUTHOR', 'ROLE_MANAGE_BOOK_DEFINITION')")
    @GetMapping(UrlConstant.Author.GET_ALL)
    public ResponseEntity<?> getAuthors(@ParameterObject PaginationFullRequestDto request) {
        return VsResponseUtil.success(authorService.findAll(request));
    }

    @Operation(summary = "API Get Author By Id")
    @PreAuthorize("hasRole('ROLE_MANAGE_AUTHOR')")
    @GetMapping(UrlConstant.Author.GET_BY_ID)
    public ResponseEntity<?> getAuthorById(@PathVariable Long id) {
        return VsResponseUtil.success(authorService.findById(id));
    }

    @Operation
    @PreAuthorize("hasRole('ROLE_MANAGE_AUTHOR')")
    @PatchMapping(UrlConstant.Author.TOGGLE_ACTIVE)
    public ResponseEntity<?> toggleActivityStatus(@PathVariable Long id,
                                                  @CurrentUser CustomUserDetails userDetails) {
        return VsResponseUtil.success(authorService.toggleActivityStatus(id, userDetails.getUserId()));
    }
}

package com.example.librarymanagement.controller;

import com.example.librarymanagement.annotation.CurrentUser;
import com.example.librarymanagement.annotation.RestApiV1;
import com.example.librarymanagement.base.VsResponseUtil;
import com.example.librarymanagement.constant.BookCondition;
import com.example.librarymanagement.constant.BookStatus;
import com.example.librarymanagement.constant.UrlConstant;
import com.example.librarymanagement.domain.dto.pagination.PaginationFullRequestDto;
import com.example.librarymanagement.security.CustomUserDetails;
import com.example.librarymanagement.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestApiV1
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Book")
public class BookController {

    BookService bookService;

    @Operation(summary = "API Update Book Status")
    @PreAuthorize("hasRole('ROLE_MANAGE_BOOK')")
    @PatchMapping(UrlConstant.Book.UPDATE_STATUS)
    public ResponseEntity<?> updateBookStatus(
            @PathVariable Long id,
            @RequestParam BookStatus status,
            @CurrentUser CustomUserDetails userDetails
            ) {
        return VsResponseUtil.success(bookService.updateStatus(id, status, userDetails.getUserId()));
    }

    @Operation(summary = "API Get All Books")
    @PreAuthorize("hasRole('ROLE_MANAGE_BOOK', 'ROLE_MANAGE_EXPORT_RECEIPT')")
    @GetMapping(UrlConstant.Book.GET_ALL)
    public ResponseEntity<?> getAllBooks(
            @ParameterObject PaginationFullRequestDto requestDto,
            @RequestParam(name = "bookCondition", required = false) BookCondition condition
            ) {
        return VsResponseUtil.success(bookService.findAll(requestDto, condition));
    }

    @Operation(summary = "API Get Book By List of IDs")
    @PreAuthorize("hasAnyRole('ROLE_MANAGE_BOOK', 'ROLE_MANAGE_EXPORT_RECEIPT')")
    @PostMapping(UrlConstant.Book.GET_BY_IDS)
    public ResponseEntity<?> getBooksByIds (@RequestBody Set<Long> ids) {
        return VsResponseUtil.success(bookService.findByIds(ids));
    }

    @Operation(summary = "API Get Books by List of Codes")
    @PreAuthorize("hasAnyRole('ROLE_MANAGE_BOOK', 'ROLE_MANAGE_BORROW_RECEIPT')")
    @PostMapping(UrlConstant.Book.GET_BY_CODES)
    public ResponseEntity<?> getBooksByCodes(@RequestBody Set<String> codes) {
        return VsResponseUtil.success(bookService.findByCodes(codes));
    }

    @Operation(summary = "API Get Book By ID")
    @PreAuthorize("hasRole('ROLE_MANAGE_BOOK')")
    @GetMapping(UrlConstant.Book.GET_BY_ID)
    public ResponseEntity<?> getBookById(@PathVariable Long id) {
        return VsResponseUtil.success(bookService.findById(id));
    }

    @Operation(summary = "API Get Book PDF")
    @PreAuthorize("hasRole('ROLE_MANAGE_BOOK')")
    @PostMapping(UrlConstant.Book.BOOK_PDF)
    public ResponseEntity<byte[]> getBookPdf(@RequestBody Set<Long> ids) {
        byte[] pdfContent = bookService.getBooksPdfContent(ids);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=book.pdf")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
                .body(pdfContent);
    }

    @Operation(summary = "API Get Book Label Type 1 PDF")
    @PreAuthorize("hasRole('ROLE_MANAGE_BOOK')")
    @PostMapping(UrlConstant.Book.BOOK_LABEL_TYPE_1_PDF)
    public ResponseEntity<byte[]> getBookLabelType1Pdf(@RequestBody Set<Long> ids) {
        byte[] pdfContent = bookService.getBooksLabelType1PdfContent(ids);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=book_label_type1.pdf")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
                .body(pdfContent);
    }

    @Operation(summary = "API Get Book Label Type 2 PDF")
    @PreAuthorize("hasRole('ROLE_MANAGE_BOOK')")
    @PostMapping(UrlConstant.Book.BOOK_LABEL_TYPE_2_PDF)
    public ResponseEntity<byte[]> getBookLabelType2Pdf(@RequestBody Set<Long> ids) {
        byte[] pdfContent = bookService.getBooksLabelType2PdfContent(ids);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=book_label_type2.pdf")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
                .body(pdfContent);
    }

    @Operation(summary = "API Get Book List PDF")
    @PreAuthorize("hasRole('ROLE_MANAE_BOOK')")
    @GetMapping(UrlConstant.Book.GET_BOOK_LIST_PDF)
    public ResponseEntity<byte[]> getBookListPdf() {
        byte[] pdfContent = bookService.generateBookListPdf();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=book_list.pdf")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_PDF_VALUE)
                .body(pdfContent);
    }

}

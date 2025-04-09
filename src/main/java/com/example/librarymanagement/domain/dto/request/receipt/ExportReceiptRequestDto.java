package com.example.librarymanagement.domain.dto.request.receipt;

import com.example.librarymanagement.constant.ErrorMessage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExportReceiptRequestDto {

    @NotBlank(message = ErrorMessage.INVALID_NOT_BLANK_FIELD)
    @Size(max = 100, message = ErrorMessage.INVALID_TEXT_LENGTH)
    private String receiptNumber;

    @NotNull(message = ErrorMessage.INVALID_SOME_THING_FIELD_IS_REQUIRED)
    private LocalDate exportDate;

    @Size(max = 100, message = ErrorMessage.INVALID_TEXT_LENGTH)
    private String exportReason;

    @NotNull(message = ErrorMessage.INVALID_NOT_NULL_FIELD)
    @Size(min = 1, max = 100, message = ErrorMessage.INVALID_ARRAY_LENGTH)
    private Set<@NotNull(message = ErrorMessage.INVALID_NOT_NULL_FIELD) Long>
        bookIds = new HashSet<>(); //ds sach
}

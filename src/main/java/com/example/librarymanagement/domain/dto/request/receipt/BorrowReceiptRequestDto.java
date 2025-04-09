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

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BorrowReceiptRequestDto {

    @NotBlank(message = ErrorMessage.INVALID_NOT_BLANK_FIELD)
    @Size(max = 100, message = ErrorMessage.INVALID_TEXT_LENGTH)
    private String receiptNumber;//Số phiếu mượn

    @NotNull(message = ErrorMessage.INVALID_SOME_THING_FIELD_IS_REQUIRED)
    private LocalDate borrowDate;

    @Size(max = 255, message = ErrorMessage.INVALID_TEXT_LENGTH)
    private String note;

    @NotNull(message = ErrorMessage.INVALID_SOME_THING_FIELD_IS_REQUIRED)
    private Long readerId;

    @NotNull(message = ErrorMessage.INVALID_SOME_THING_FIELD_IS_REQUIRED)
    @Size(min = 1, max = 100, message = ErrorMessage.INVALID_ARRAY_LENGTH)
    private Set<
            @NotBlank(message = ErrorMessage.INVALID_NOT_BLANK_FIELD)
            @Size(max = 100, message = ErrorMessage.INVALID_TEXT_LENGTH)
            String
            > books = new HashSet<>();


}

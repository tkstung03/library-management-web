package com.example.librarymanagement.domain.dto.request.auth;

import com.example.librarymanagement.constant.ErrorMessage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReaderLoginRequestDto {
    @NotBlank(message = ErrorMessage.INVALID_NOT_BLANK_FIELD)
    @Size(max = 100, message = ErrorMessage.INVALID_TEXT_LENGTH)
    private String cardNumber;

    @NotBlank(message = ErrorMessage.INVALID_NOT_BLANK_FIELD)
    @Size(max = 100, message = ErrorMessage.INVALID_TEXT_LENGTH)
    private String password;
}

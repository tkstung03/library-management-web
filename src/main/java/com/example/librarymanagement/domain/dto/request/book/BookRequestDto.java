package com.example.librarymanagement.domain.dto.request.book;

import com.example.librarymanagement.constant.ErrorMessage;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookRequestDto {
    @NotNull(message = ErrorMessage.INVALID_SOME_THING_FIELD_IS_REQUIRED)
    private Long bookDefinitionId;

    @NotNull(message = ErrorMessage.INVALID_SOME_THING_FIELD_IS_REQUIRED)
    @Min(value = 1, message = ErrorMessage.INVALID_MINIMUM_ONE)
    @Max(value = 100, message = ErrorMessage.INVALID_MAXIMUM_ONE_HUNDRED)
    private Integer quantity;
}

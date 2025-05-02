package com.example.librarymanagement.domain.dto.request.review;

import com.example.librarymanagement.constant.ErrorMessage;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateReviewRequestDto {
    @NotNull(message = ErrorMessage.INVALID_SOME_THING_FIELD_IS_REQUIRED)
    @Min(value = 1, message = ErrorMessage.INVALID_MINIMUM_ONE)
    @Max(value = 5, message = ErrorMessage.INVALID_MAXIMUM_FIVE)
    private Integer rating;

    @Size(max = 500, message = ErrorMessage.INVALID_TEXT_LENGTH)
    private String comment;

    @NotNull(message = ErrorMessage.INVALID_SOME_THING_FIELD_IS_REQUIRED)
    private Long bookDefinitionId;
}

package com.example.librarymanagement.domain.dto.request.reader;

import com.example.librarymanagement.constant.ErrorMessage;
import com.example.librarymanagement.constant.PunishmentForm;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReaderViolationRequestDto {

    @NotBlank(message = ErrorMessage.INVALID_NOT_BLANK_FIELD)
    @Size(max = 100, message = ErrorMessage.INVALID_TEXT_LENGTH)
    private String violationDetails;

    @NotNull(message = ErrorMessage.INVALID_SOME_THING_FIELD_IS_REQUIRED)
    private PunishmentForm punishmentForm;

    @Size(max = 100, message = ErrorMessage.INVALID_TEXT_LENGTH)
    private String otherPenaltyForm;

    @NotNull(message = ErrorMessage.INVALID_SOME_THING_FIELD_IS_REQUIRED)
    private LocalDate penaltyDate; //ngay phat

    private LocalDate endDate; //ngay ket thuc phat(neu co)

    @Min(value = 0, message = ErrorMessage.INVALID_MINIMUM_ZERO)
    private Double fineAmount; //tien phat

    @Size(max = 255, message = ErrorMessage.INVALID_TEXT_LENGTH)
    private String notes;

    @NotNull(message = ErrorMessage.INVALID_SOME_THING_FIELD_IS_REQUIRED)
    private Long readerId; //id ban doc vi pham
}

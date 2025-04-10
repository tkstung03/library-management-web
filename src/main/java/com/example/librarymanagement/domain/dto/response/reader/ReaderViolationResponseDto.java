package com.example.librarymanagement.domain.dto.response.reader;

import com.example.librarymanagement.constant.PunishmentForm;
import com.example.librarymanagement.domain.entity.Reader;
import com.example.librarymanagement.domain.entity.ReaderViolation;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ReaderViolationResponseDto {

    private final long id;

    private final String violationDetails;

    private final PunishmentForm punishmentForm;

    private final String otherPunishmentForm;

    private final LocalDate penaltyDate;

    private final LocalDate endDate;

    private final Double fineAmount;

    private final String notes;

    private final long readerId;

    private final String cardNumber;

    private final String fullName;

    public ReaderViolationResponseDto(ReaderViolation readerViolation){
        this.id = readerViolation.getId();
        this.violationDetails = readerViolation.getViolationDetails();
        this.punishmentForm = readerViolation.getPunishmentForm();
        this.otherPunishmentForm = readerViolation.getOtherPunishmentForm();
        this.penaltyDate = readerViolation.getPenaltyDate();
        this.endDate = readerViolation.getEndDate();
        this.fineAmount = readerViolation.getFineAmount();
        this.notes = readerViolation.getNotes();
        this.readerId = readerViolation.getReader().getId();
        this.cardNumber = readerViolation.getReader().getCardNumber();
        this.fullName = readerViolation.getReader().getFullName();
    }
}

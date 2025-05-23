package com.example.librarymanagement.domain.dto.response.library;

import com.example.librarymanagement.constant.CommonConstant;
import com.example.librarymanagement.domain.entity.LibraryVisit;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class LibraryVisitResponseDto {

    private final long id;

    private final String cardNumber;

    private final String fullName;

    private final String cardType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CommonConstant.PATTERN_DATE_TIME)
    private final LocalDateTime entryTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = CommonConstant.PATTERN_DATE_TIME)
    private final LocalDateTime exitTime;

    private final String major;

    public LibraryVisitResponseDto(LibraryVisit libraryVisit){
        this.id = libraryVisit.getId();
        this.cardNumber = libraryVisit.getReader().getCardNumber();
        this.fullName = libraryVisit.getReader().getFullName();
        this.cardType = libraryVisit.getReader().getCardType().getDisplayName();
        this.entryTime = libraryVisit.getEntryTime();
        this.exitTime = libraryVisit.getExitTime();
        this.major = libraryVisit.getReader().getMajor() != null
                ? libraryVisit.getReader().getMajor().getName()
                : null;
    }

}

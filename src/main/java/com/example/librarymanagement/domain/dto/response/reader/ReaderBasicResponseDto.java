package com.example.librarymanagement.domain.dto.response.reader;

import com.example.librarymanagement.constant.CardStatus;
import com.example.librarymanagement.domain.entity.Reader;
import lombok.Getter;

@Getter
public class ReaderBasicResponseDto {

    private final long id;

    private final String fullName;

    private final String cardNumber;

    private final CardStatus status;

    public ReaderBasicResponseDto(Reader reader) {
        this.id = reader.getId();
        this.fullName = reader.getFullName();
        this.cardNumber = reader.getCardNumber();
        this.status = reader.getStatus();
    }
}

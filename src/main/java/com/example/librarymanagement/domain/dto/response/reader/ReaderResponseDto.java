package com.example.librarymanagement.domain.dto.response.reader;

import com.example.librarymanagement.constant.BorrowStatus;
import com.example.librarymanagement.constant.CardStatus;
import com.example.librarymanagement.constant.CardType;
import com.example.librarymanagement.constant.Gender;
import com.example.librarymanagement.domain.dto.response.major.MajorResponseDto;
import com.example.librarymanagement.domain.entity.Reader;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ReaderResponseDto {

    private final long id;

    private final CardType cardType;

    private final String fullName;

    private final LocalDate dateOfBirth;

    private final Gender gender;

    private final String avatar;

    private final String address;

    private final String email;

    private final String phoneNumber;

    private final String cardNumber;

    private final LocalDate expiryDate;

    private final CardStatus status;

    private final long currentBorrowedBooks;

    private final long libraryVisitCount;

    private final MajorResponseDto major;

    public ReaderResponseDto(Reader reader) {

        this.id = reader.getId();
        this.cardType = reader.getCardType();
        this.fullName = reader.getFullName();
        this.dateOfBirth = reader.getDateOfBirth();
        this.gender = reader.getGender();
        this.avatar = reader.getAvatar();
        this.address = reader.getAddress();
        this.email = reader.getEmail();
        this.phoneNumber = reader.getPhoneNumber();
        this.cardNumber = reader.getCardNumber();
        this.expiryDate = reader.getExpiryDate();
        this.status = reader.getStatus();

        //so phieu muon
        this.currentBorrowedBooks = reader.getBorrowReceipts()
                .stream()
                .filter(borrowReceipt -> !borrowReceipt.getStatus().equals(BorrowStatus.RETURNED)).
                count();

        //so luot vao thu vien
        this.libraryVisitCount = reader.getLibraryVisits().size();

        this.major = reader.getMajor() != null ? new MajorResponseDto(reader.getMajor()) : null;
    }
}
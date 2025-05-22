package com.example.librarymanagement.domain.entity;

import com.example.librarymanagement.constant.CardStatus;
import com.example.librarymanagement.constant.CardType;
import com.example.librarymanagement.constant.Gender;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "readers",
        uniqueConstraints = {
            @UniqueConstraint(name = "UN_READER_CARD_NUMBER", columnNames = "card_number"),
            @UniqueConstraint(name = "UN_READER_EMAIL", columnNames = "email")
        })
public class Reader {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reader_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "card_type", nullable = false, length = 20)
    private CardType cardType;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", length = 8)
    private Gender gender;

    @Column(name = "avatar")
    private String avatar; // Ảnh đại diện (URL hoặc file path)

    @Column(name = "address")
    private String address;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "phone_number", length = 15)
    private String phoneNumber;

    @Column(name = "card_number", nullable = false, unique = true, length = 20)
    private String cardNumber;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "created_date", nullable = false)
    private LocalDate createdDate;

    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate; //ngày hết hạn thẻ

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CardStatus status; // Trạng thái thẻ

    @OneToOne(mappedBy = "reader", cascade = CascadeType.ALL)
    @JsonIgnore
    private Cart cart;

    @OneToMany(mappedBy = "reader", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<BorrowReceipt> borrowReceipts = new ArrayList<>();

    @OneToMany(mappedBy = "reader",cascade = CascadeType.ALL)
    @JsonIgnore
    private List<LibraryVisit> libraryVisits = new ArrayList<>();

    @OneToMany(mappedBy = "reader", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<ReaderViolation> readerViolations = new ArrayList<>();

    @OneToMany(mappedBy = "reader", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Review> reviews = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "major_id", foreignKey = @ForeignKey(name = "FK_READER_MAJOR_ID"), referencedColumnName = "major_id")
    @JsonIgnore
    private Major major;
}

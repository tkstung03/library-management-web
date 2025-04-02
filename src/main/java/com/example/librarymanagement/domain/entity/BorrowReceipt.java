package com.example.librarymanagement.domain.entity;

import com.example.librarymanagement.constant.BookBorrowStatus;
import com.example.librarymanagement.constant.BorrowStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "borrow_receipts",
        uniqueConstraints = @UniqueConstraint(name = "UN_BORROW_RECEIPTS_RECEIPT_NUMBER", columnNames = "receipt_number"))
public class BorrowReceipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "borrow_receipt_id")
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "receipt_number")
    private String receiptNumber;

    @Column(name = "borrow_date")
    private LocalDate borrowDate; //ngày mượn

    @Column(name = "due_date")
    private LocalDate dueDate; //ngày hẹn trả

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BorrowStatus status;

    @Column(name = "note", length = 500)
    private String note;

    @OneToMany(mappedBy = "borrowReceipt", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<BookBorrow> bookBorrows = new ArrayList<>(); //các sách mượn

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reader_id", foreignKey = @ForeignKey(name = "FK_BORROW_RECEIPTS_READER_ID"), referencedColumnName = "reader_id", nullable = false)
    @JsonIgnore
    private Reader reader; //bạn đọc
}

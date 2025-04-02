package com.example.librarymanagement.domain.entity;

import com.example.librarymanagement.constant.PunishmentForm;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reader_violations")
public class ReaderViolation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reader_violation_id")
    private Long id;

    @Column(name = "violation_details", nullable = false)
    private String violationDetails; // Nội dung vi phạm

    @Enumerated(EnumType.STRING)
    @Column(name = "punishment_form")
    private PunishmentForm punishmentForm; //hình thức phạt

    @Column(name = "other_punishment_form")
    private String otherPunishmentForm; // hình thức phạt khác

    @Column(name = "penalty_date")
    private LocalDate penaltyDate; // Ngày phạt

    @Column(name = "end_date")
    private LocalDate endDate; // Ngày kết thúc

    @Column(name = "fine_amount")
    private Double fineAmount; // Số tiền phạt

    @Column(name = "notes")
    private String notes;

    @ManyToOne
    @JoinColumn(name = "reader_id", foreignKey = @ForeignKey(name = "FK_VIOLATION_READER_ID"), referencedColumnName = "reader_id", nullable = false)
    @JsonIgnore
    private Reader reader;
}

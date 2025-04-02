package com.example.librarymanagement.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "book_sets",
        uniqueConstraints = @UniqueConstraint(name = "UN_BOOK_SET_NAME", columnNames = "name"))
public class BookSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_set_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "active_flag", nullable = false)
    private Boolean activeFlag = Boolean.TRUE;

    @OneToMany(mappedBy = "bookSet", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<BookDefinition> bookDefinitions = new ArrayList<>();
}

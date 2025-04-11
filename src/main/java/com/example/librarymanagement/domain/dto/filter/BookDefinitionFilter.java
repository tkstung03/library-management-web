package com.example.librarymanagement.domain.dto.filter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BookDefinitionFilter {
    private String bookCode;

    private String title;

    private String keyword;

    private Integer publishingYear;

    private String author;
}

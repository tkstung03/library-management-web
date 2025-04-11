package com.example.librarymanagement.domain.dto.response.statistics;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PublicationResponseDto {
    private String title;

    private long borrowCount;
}

package com.example.librarymanagement.domain.dto.response.other;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SlideResponseDto {
    private String id;
    private String title;
    private String description;
    private String imageUrl;
    private String activeFlag;
}

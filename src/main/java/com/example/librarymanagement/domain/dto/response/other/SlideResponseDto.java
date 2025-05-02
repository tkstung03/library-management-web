package com.example.librarymanagement.domain.dto.response.other;

import com.example.librarymanagement.domain.entity.Book;
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
    private Boolean activeFlag;
}

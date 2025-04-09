package com.example.librarymanagement.domain.dto.request.other;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SlideRequestDto {
    private String title = "";

    private String description = "";

    private boolean activeFlag = true;
}

package com.example.librarymanagement.domain.dto.response.major;

import com.example.librarymanagement.domain.entity.Major;
import lombok.Getter;

@Getter
public class MajorResponseDto {
    private Long id;

    private String name;

    private Boolean activeFlag;

    private long readers;

    public MajorResponseDto(Major major) {
        this.id = major.getId();
        this.name = major.getName();
        this.activeFlag = major.getActiveFlag();
        this.readers = major.getReaders().size();
    }
}

package com.example.librarymanagement.domain.dto.response.statistics;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TimeSeriesData {
    private long timestamp;

    private int value;
}

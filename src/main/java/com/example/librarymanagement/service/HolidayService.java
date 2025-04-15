package com.example.librarymanagement.service;

import com.example.librarymanagement.domain.dto.common.CommonResponseDto;
import com.example.librarymanagement.domain.dto.request.holiday.HolidayRequestDto;
import com.example.librarymanagement.domain.dto.response.holiday.HolidayResponseDto;

import java.util.List;

public interface HolidayService {

    List<HolidayResponseDto> getAllHolidays(Boolean activeFlag);

    HolidayResponseDto getHolidayById(String id);

    CommonResponseDto addHoliday(HolidayRequestDto holidayRequestDto, String userId);

    CommonResponseDto updateHoliday(String id, HolidayRequestDto holidayRequestDto, String userId);

    CommonResponseDto deleteHoliday(String id);
}

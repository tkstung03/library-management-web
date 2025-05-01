package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.constant.EventConstants;
import com.example.librarymanagement.constant.SuccessMessage;
import com.example.librarymanagement.domain.dto.common.CommonResponseDto;
import com.example.librarymanagement.domain.dto.request.holiday.HolidayRequestDto;
import com.example.librarymanagement.domain.dto.response.holiday.HolidayResponseDto;
import com.example.librarymanagement.service.HolidayService;
import com.example.librarymanagement.service.LogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class HolidayServiceImpl implements HolidayService {

    private static final String HOLIDAYS_FILE_PATH = "data/holidays.txt";
    private static final String TAG = "Nghỉ lễ";

    private final LogService logService;
    private final MessageSource messageSource;

    public List<HolidayResponseDto> readFromFile(String filePath) {
        List<HolidayResponseDto> holidays = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));

            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    String id = parts[0];
                    String name = parts[1];
                    LocalDate startDate = LocalDate.parse(parts[2]);
                    LocalDate endDate = LocalDate.parse(parts[3]);
                    Boolean activeFlag = Boolean.valueOf(parts[4]);

                    holidays.add(new HolidayResponseDto(id, name, startDate, endDate, activeFlag));
                }
            }
        } catch (IOException e) {
            log.error("Error reading holiday file: {}", filePath, e);
        }
        return holidays;
    }

    public void writeToFile(String filePath, List<HolidayResponseDto> holidays) {
        List<String> lines = new ArrayList<>();
        for (HolidayResponseDto holiday : holidays) {
            String holidayData = holiday.getId() + ","
                    + holiday.getName() + ","
                    + holiday.getStartDate() + ","
                    + holiday.getEndDate() + ","
                    + holiday.getActiveFlag();
            lines.add(holidayData);
        }

        try {
            Files.write(Paths.get(filePath), lines);
        } catch (IOException e) {
            log.error("Error writing holiday file: {}", filePath, e);
        }
    }

    private HolidayResponseDto findById(String id, List<HolidayResponseDto> holidays) {
        return holidays.stream().filter(dto -> dto.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public List<HolidayResponseDto> getAllHolidays(Boolean activeFlag) {
        return readFromFile(HOLIDAYS_FILE_PATH).stream()
                .filter(dto -> activeFlag == null || dto.getActiveFlag().equals(activeFlag))
                .toList();
    }

    @Override
    public HolidayResponseDto getHolidayById(String id) {
        List<HolidayResponseDto> holidays = readFromFile(HOLIDAYS_FILE_PATH);
        return findById(id, holidays);
    }

    @Override
    public CommonResponseDto addHoliday(HolidayRequestDto holidayRequestDto, String userId) {
        List<HolidayResponseDto> holidays = readFromFile(HOLIDAYS_FILE_PATH);

        HolidayResponseDto newHoliday = new HolidayResponseDto();
        newHoliday.setId(String.valueOf(holidays.size()));
        newHoliday.setName(holidayRequestDto.getName());
        newHoliday.setStartDate(holidayRequestDto.getStartDate());
        newHoliday.setEndDate(holidayRequestDto.getEndDate());
        newHoliday.setActiveFlag(holidayRequestDto.getActiveFlag());
        holidays.add(newHoliday);

        writeToFile(HOLIDAYS_FILE_PATH, holidays);

        logService.createLog(TAG, EventConstants.ADD, "Thêm ngày nghỉ lễ mới: " + holidayRequestDto.getName() + ", Ngày: " + holidayRequestDto.getStartDate(), userId);

        String message = messageSource.getMessage(SuccessMessage.CREATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message, newHoliday);
    }

    @Override
    public CommonResponseDto updateHoliday(String id, HolidayRequestDto holidayRequestDto, String userId) {
        List<HolidayResponseDto> holidays = readFromFile(HOLIDAYS_FILE_PATH);
        HolidayResponseDto holidayToUpdate = findById(id, holidays);
        if (holidayToUpdate == null) {
            return new CommonResponseDto("Holiday with ID " + id + " not found.");
        }

        holidayToUpdate.setName(holidayRequestDto.getName());
        holidayToUpdate.setStartDate(holidayRequestDto.getStartDate());
        holidayToUpdate.setEndDate(holidayRequestDto.getEndDate());
        holidayToUpdate.setActiveFlag(holidayRequestDto.getActiveFlag());
        writeToFile(HOLIDAYS_FILE_PATH, holidays);

        logService.createLog(TAG, EventConstants.EDIT, "Cập nhật kỳ nghỉ: " + holidayRequestDto.getName() + " (" + holidayRequestDto.getStartDate() + " - " + holidayRequestDto.getEndDate() + ")", userId);

        String message = messageSource.getMessage(SuccessMessage.UPDATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message, holidayToUpdate);
    }

    @Override
    public CommonResponseDto deleteHoliday(String id) {
        List<HolidayResponseDto> holidays = readFromFile(HOLIDAYS_FILE_PATH);
        HolidayResponseDto holidayToDelete = findById(id, holidays);
        if (holidayToDelete == null) {
            return new CommonResponseDto("Holiday with ID " + id + " not found.");
        }

        holidays.remove(holidayToDelete);
        writeToFile(HOLIDAYS_FILE_PATH, holidays);

        logService.createLog(TAG, EventConstants.DELETE, "Xóa kỳ nghỉ với ID: " + id, null);

        String message = messageSource.getMessage(SuccessMessage.DELETE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }
}

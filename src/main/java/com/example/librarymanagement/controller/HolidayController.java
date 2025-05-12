package com.example.librarymanagement.controller;

import com.example.librarymanagement.annotation.CurrentUser;
import com.example.librarymanagement.annotation.RestApiV1;
import com.example.librarymanagement.base.VsResponseUtil;
import com.example.librarymanagement.constant.UrlConstant;
import com.example.librarymanagement.domain.dto.request.holiday.HolidayRequestDto;
import com.example.librarymanagement.security.CustomUserDetails;
import com.example.librarymanagement.service.HolidayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestApiV1
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Holiday Management")
public class HolidayController {
    HolidayService holidayService;

    @Operation(summary = "API Get All Holidays")
    @GetMapping(UrlConstant.SystemSetting.GET_ALL_HOLIDAYS)
    public ResponseEntity<?> getAllHolidays(@RequestParam(required = false) Boolean activeFlag) {
        return VsResponseUtil.success(holidayService.getAllHolidays(activeFlag));
    }

    @Operation(summary = "API Get Holiday by ID")
    @GetMapping(UrlConstant.SystemSetting.GET_HOLIDAY_BY_ID)
    public ResponseEntity<?> getHolidayById(@PathVariable String id) {
        return VsResponseUtil.success(holidayService.getHolidayById(id));
    }

    @Operation(summary = "API Add a New Holiday")
    @PreAuthorize("hasRole('ROLE_MANAGE_SYSTEM_SETTINGS')")
    @PostMapping(UrlConstant.SystemSetting.ADD_HOLIDAY)
    public ResponseEntity<?> addHoliday(
            @Valid @RequestBody HolidayRequestDto holidayRequestDto,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(HttpStatus.CREATED, holidayService.addHoliday(holidayRequestDto, userDetails.getUserId()));
    }

    @Operation(summary = "API Update Holiday")
    @PreAuthorize("hasRole('ROLE_MANAGE_SYSTEM_SETTINGS')")
    @PutMapping(UrlConstant.SystemSetting.UPDATE_HOLIDAY)
    public ResponseEntity<?> updateHoliday(
            @PathVariable String id,
            @Valid @RequestBody HolidayRequestDto holidayRequestDto,
            @CurrentUser CustomUserDetails userDetails
    ) {
        return VsResponseUtil.success(holidayService.updateHoliday(id, holidayRequestDto, userDetails.getUserId()));
    }

    @Operation(summary = "API Delete Holiday")
    @PreAuthorize("hasRole('ROLE_MANAGE_SYSTEM_SETTINGS')")
    @DeleteMapping(UrlConstant.SystemSetting.DELETE_HOLIDAY)
    public ResponseEntity<?> deleteHoliday(@PathVariable String id) {
        return VsResponseUtil.success(holidayService.deleteHoliday(id));
    }
}

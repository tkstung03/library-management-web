package com.example.librarymanagement.controller;

import com.example.librarymanagement.annotation.CurrentUser;
import com.example.librarymanagement.annotation.RestApiV1;
import com.example.librarymanagement.base.VsResponseUtil;
import com.example.librarymanagement.constant.UrlConstant;
import com.example.librarymanagement.security.CustomUserDetails;
import com.example.librarymanagement.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestApiV1
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Notifications")
public class NotificationController {
    NotificationService notificationService;

    @Operation(summary = "API Get Notification")
    @GetMapping(UrlConstant.Notification.GET_BY_USER)
    public ResponseEntity<?> getUserNotifications(@CurrentUser CustomUserDetails userDetails) {
        return VsResponseUtil.success(notificationService.getUserNotifications(userDetails.getUserId()));
    }

    @Operation(summary = "API Mark Notification As Read")
    @PutMapping(UrlConstant.Notification.MARK_AS_READ)
    public ResponseEntity<?> markAsRead(@PathVariable Long notificationId, @CurrentUser CustomUserDetails userDetails) {
        return VsResponseUtil.success(notificationService.markAsRead(notificationId, userDetails.getUserId()));
    }

    @Operation(summary = "API Delete Notification")
    @DeleteMapping(UrlConstant.Notification.DELETE)
    public ResponseEntity<?> deleteNotifications(@PathVariable Long notificationId, @CurrentUser CustomUserDetails userDetails) {
        return VsResponseUtil.success(notificationService.deleteNotifications(notificationId, userDetails.getUserId()));
    }

}

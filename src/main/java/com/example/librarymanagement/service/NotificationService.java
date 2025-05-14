package com.example.librarymanagement.service;

import com.example.librarymanagement.domain.dto.common.CommonResponseDto;
import com.example.librarymanagement.domain.entity.Notifications;

import java.util.List;

public interface NotificationService {

    Notifications createNotifications(String userId, String title, String message);

    List<Notifications> getUserNotifications(String userId);

    CommonResponseDto markAsRead(Long notificationId, String userId);

    CommonResponseDto deleteNotifications(Long notificationId, String userId);
}

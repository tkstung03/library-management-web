package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.constant.ErrorMessage;
import com.example.librarymanagement.constant.SuccessMessage;
import com.example.librarymanagement.domain.dto.common.CommonResponseDto;
import com.example.librarymanagement.domain.entity.Notifications;
import com.example.librarymanagement.domain.entity.User;
import com.example.librarymanagement.exception.NotFoundException;
import com.example.librarymanagement.repository.NotificationRepository;
import com.example.librarymanagement.repository.UserRepository;
import com.example.librarymanagement.service.NotificationService;
import com.example.librarymanagement.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    private final UserRepository userRepository;

    private final MessageUtil messageUtil;

    private Notifications getEntity(Long notificationId, String userId) {
        return notificationRepository.findByIdAndUser_Id(notificationId, userId)
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Notification.ERR_NOT_FOUND_ID, userId));
    }
    @Override
    public Notifications createNotification(String userId, String title, String message) {
        User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new NotFoundException(ErrorMessage.User.ERR_NOT_FOUND_ID, userId));

        Notifications notifications = new Notifications();
        notifications.setUser(user);
        notifications.setTitle(title);
        notifications.setMessage(message);
        notifications.setRead(false);

        return notificationRepository.save(notifications);
    }

    @Override
    public List<Notifications> getUserNotifications(String userId) {
        return notificationRepository.findByUser_Id(userId);
    }

    @Override
    public CommonResponseDto markAsRead(Long notificationId, String userId) {
        Notifications notifications = getEntity(notificationId, userId);

        notifications.setRead(true);
        notificationRepository.save(notifications);

        String message = messageUtil.getMessage(SuccessMessage.UPDATE);
        return new CommonResponseDto(message);
    }

    @Override
    public CommonResponseDto deleteNotification(Long notificationId, String userId) {
        Notifications notifications = getEntity(notificationId, userId);

        notificationRepository.delete(notifications);

        String message = messageUtil.getMessage(SuccessMessage.DELETE);
        return new CommonResponseDto(message);
    }
}

package org.example.service;

import org.example.dto.*;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface NotificationService {
    public NotificationResponseDto sendMessage(NotificationSendRequestDto requestDto);
    public NotificationHistoryResponseDto getNotificationHistory(Long userId, String type, Pageable pageable);
    public NotificationDetailResponseDto getNotification(Long id);
    public NotificationDetailResponseDto notificationMarkAsRead(Long id);
    public RetryResponseDto retrySend(RetryRequestDto requestDto);




}

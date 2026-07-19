package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.domain.Notification;
import org.example.domain.NotificationChannelResults;
import org.example.domain.UserPreferences;
import org.example.dto.*;
import org.example.enumeration.NotificationStatus;
import org.example.enumeration.PreferedChannel;
import org.example.enumeration.RetryStatus;
import org.example.mapper.NotificationMapper;
import org.example.mapper.UserPreferencesMapper;
import org.example.repository.NotificationDao;
import org.example.repository.UserPreferencesDao;
import org.example.service.NotificationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final UserPreferencesDao userPreferencesDao;
    private final NotificationMapper notificationMapper;
    private final NotificationDao notificationDao;
    private final NotificationService notificationService;


    @Override
    public NotificationResponseDto sendMessage(NotificationSendRequestDto requestDto) {
        NotificationResponseDto responseDto = new NotificationResponseDto();

        UserPreferences preferences = userPreferencesDao.findByUserId(requestDto.getUserId()).orElse(null);

        if (preferences == null) {
            preferences = new UserPreferences();
            preferences.setUserId(requestDto.getUserId());
            preferences.setEmailEnabled(true);
            preferences.setPreferedChannel(PreferedChannel.EMAIL);
            userPreferencesDao.save(preferences);
        }

        List<PreferedChannel> activeChannels = new ArrayList<>();

        if (preferences.isEmailEnabled()) {
            activeChannels.add(PreferedChannel.EMAIL);
        }
        if (preferences.isSmsEnabled()) {
            activeChannels.add(PreferedChannel.SMS);
        }
        if (preferences.isPushEnabled()) {
            activeChannels.add(PreferedChannel.PUSH);
        }
        if (preferences.isTelegramEnabled()) {
            activeChannels.add(PreferedChannel.TELEGRAM);
        }

        Notification notification = notificationMapper.toEntity(requestDto);
        notificationDao.save(notification);

        for (PreferedChannel channel : activeChannels) {
            NotificationChannelResults channelResult = new NotificationChannelResults();
            channelResult.setNotification(notification);
            if (Math.random() >= 0.95) {
                channelResult.setSuccess(false);
                channelResult.setErrorMessage("Failed to send via " + channel);
                notification.setStatus(NotificationStatus.FAILED);
            } else {
                channelResult.setSuccess(true);
                notification.setStatus(NotificationStatus.SENT);
            }
            notification.getChannelResults().add(channelResult);
        }
        notificationDao.save(notification);

        List<ChannelResultDto> channelResultDtos = new ArrayList<>();

        for (NotificationChannelResults resultEntity : notification.getChannelResults()) {
            ChannelResultDto dto = new ChannelResultDto();
            dto.setChannel(resultEntity.getChannel());
            dto.setSuccess(resultEntity.isSuccess());
            dto.setErrorMessage(resultEntity.getErrorMessage());
            dto.setNotificationId(resultEntity.getNotification().getId());
            channelResultDtos.add(dto);
        }
        responseDto.setChannelResults(channelResultDtos);

        boolean isAnySuccess = channelResultDtos.stream().anyMatch(ChannelResultDto::isSuccess);
        responseDto.setSent(isAnySuccess);
        responseDto.setSentAt(LocalDateTime.now());

        return responseDto;
    }

    @Override
    public NotificationHistoryResponseDto getNotificationHistory(Long userId, String type, Pageable pageable) {
        Page<Notification> page;
        if (type == null) {
            page = notificationDao.findByUserId(userId, pageable);
        } else {
            page = notificationDao.findByUserIdAndType(userId, type, pageable);
        }

        List<Notification> entities = page.getContent();

        List<NotificationHistoryDto> dtoList = entities.stream()
                .map(notificationMapper::toHistoryDto)
                .toList();
        NotificationHistoryResponseDto responseDto = new NotificationHistoryResponseDto();
        responseDto.setUserId(userId);
        responseDto.setNotifications(dtoList);
        responseDto.setTotalCount(page.getTotalElements());
        responseDto.setHasMore(page.hasNext());
        return responseDto;
    }

    @Override
    public NotificationDetailResponseDto getNotification(Long id) {
        Notification notification = notificationDao.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notification с ID " + id + " не найден"));
        return notificationMapper.toDetailDto(notification);
    }

    @Override
    @Transactional
    public NotificationDetailResponseDto notificationMarkAsRead(Long id) {
        Notification notification = notificationDao.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notification с ID " + id + " не найден"));
        notification.setRead(true);
        notificationDao.save(notification);
        return notificationMapper.toDetailDto(notification);
    }

    @Override
    @Transactional
    public RetryResponseDto retrySend(RetryRequestDto requestDto) {
        List<RetryResultsDto> results = new ArrayList<>();
        int failedCount = 0;
        int successCount = 0;

        for (Long id : requestDto.getNotificationIds()) {
            try {
                Notification notification = notificationDao.findById(id)
                        .orElseThrow(() -> new RuntimeException("Notification not found"));

                if (notification.getStatus().name().equals("FAILED")){
                    notificationService.sendMessage(notificationMapper.toSendRequestDto(notification));
                    if (notification.getStatus().name().equals("SENT")){
                        successCount++;
                        results.add(new RetryResultsDto(id, true, null, RetryStatus.SENT));
                        notificationDao.save(notification);
                    }else {
                        notificationDao.save(notification);
                        throw new RuntimeException("Notification failed");
                    }
                } else if (notification.getStatus().name().equals("SENT") ||notification.getStatus().name().equals("PENDING") ) {
                    if (requestDto.isForceRetry()){
                        notificationService.sendMessage(notificationMapper.toSendRequestDto(notification));
                        if (notification.getStatus().name().equals("SENT")){
                            successCount++;
                            results.add(new RetryResultsDto(id, true, null, RetryStatus.SENT));
                            notificationDao.save(notification);
                        }else {
                            notificationDao.save(notification);
                            throw new RuntimeException("Notification failed");
                        }
                    }else {
                        throw new RuntimeException("Notification is not in FAILED status and forceRetry is false");
                    }
                }
            } catch (Exception e){
                failedCount++;
                results.add(new RetryResultsDto(id, false, null, RetryStatus.FAILED));
            }

        }
        return new RetryResponseDto(requestDto.getNotificationIds().size(), successCount, failedCount, results);
    }


}

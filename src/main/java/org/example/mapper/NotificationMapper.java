package org.example.mapper;

import lombok.RequiredArgsConstructor;
import org.example.domain.Notification;
import org.example.domain.NotificationChannelResults;
import org.example.dto.*;
import org.example.enumeration.NotificationStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class NotificationMapper {

    public NotificationSendRequestDto toSendRequestDto(Notification notification){
        NotificationSendRequestDto requestDto = new NotificationSendRequestDto();
        requestDto.setOrderId(notification.getOrderId());
        requestDto.setUserId(notification.getUserId());
        requestDto.setUserEmail(notification.getUserEmail());
        requestDto.setEventType(notification.getEventType());
        requestDto.setSubject(notification.getSubject());
        requestDto.setMessage(notification.getMessage());
        requestDto.setTotalAmount(notification.getTotalAmount());
        requestDto.setTrackingNumber(notification.getTrackingNumber());
        requestDto.setReason(notification.getReason());
        return requestDto;
    }

    public Notification toEntity(NotificationSendRequestDto requestDto) {
        Notification notification = new Notification();
        notification.setOrderId(requestDto.getOrderId());
        notification.setUserId(requestDto.getUserId());
        notification.setUserEmail(requestDto.getUserEmail());
        notification.setEventType(requestDto.getEventType());
        notification.setSubject(requestDto.getSubject());
        notification.setMessage(requestDto.getMessage());
        notification.setTotalAmount(requestDto.getTotalAmount());
        notification.setTrackingNumber(requestDto.getTrackingNumber());
        notification.setReason(requestDto.getReason());

        return notification;
    }

    public NotificationResponseDto toResponseDto(Notification notification, List<ChannelResultDto> channelResult) {
        NotificationResponseDto notificationResponseDto = new NotificationResponseDto();
        notificationResponseDto.setSent(notification.isSent());
        notificationResponseDto.setChannelResults(channelResult);
        notificationResponseDto.setSentAt(notification.getSentAt());
        return notificationResponseDto;
    }

    public NotificationHistoryDto toHistoryDto(Notification notification) {
        NotificationHistoryDto dto = new NotificationHistoryDto();

        dto.setId(notification.getId());
        dto.setOrderId(notification.getOrderId());

        if (notification.getEventType() != null) {
            dto.setType(notification.getEventType().name());
        }

        dto.setSubject(notification.getSubject());
        dto.setMessage(notification.getMessage());
        dto.setSentAt(notification.getSentAt());


        if (notification.getChannelResults() != null && !notification.getChannelResults().isEmpty()) {
            NotificationChannelResults result = notification.getChannelResults().get(0);

            dto.setChannel(result.getChannel());


            if (result.isSuccess()) {
                dto.setStatus(NotificationStatus.SENT);
            } else {
                dto.setStatus(NotificationStatus.FAILED);
            }
        } else {

            dto.setStatus(NotificationStatus.PENDING);
        }

        return dto;
    }

    public NotificationSummaryResponseDto toSummaryDto(Notification notification) {
        NotificationSummaryResponseDto summaryResponseDto = new NotificationSummaryResponseDto();
        summaryResponseDto.setId(notification.getId());
        summaryResponseDto.setOrderId(notification.getOrderId());
        summaryResponseDto.setType(notification.getEventType().name());
        summaryResponseDto.setSubject(notification.getSubject());
        summaryResponseDto.setMessage(notification.getMessage());

        if (notification.getChannelResults() != null && !notification.getChannelResults().isEmpty()) {

            summaryResponseDto.setChannel(notification.getChannelResults().get(0).getChannel());
        }
        summaryResponseDto.setStatus(notification.getStatus());
        summaryResponseDto.setSentAt(notification.getSentAt());
        return summaryResponseDto;

    }


    public NotificationDetailResponseDto toDetailDto(Notification notification) {
        NotificationSummaryResponseDto summaryResponseDto = toSummaryDto(notification);
        NotificationDetailResponseDto detailDto = new NotificationDetailResponseDto();
        detailDto.setId(summaryResponseDto.getId());
        detailDto.setOrderId(summaryResponseDto.getOrderId());
        detailDto.setType(summaryResponseDto.getType());
        detailDto.setSubject(summaryResponseDto.getSubject());
        detailDto.setMessage(summaryResponseDto.getMessage());
        detailDto.setChannel(summaryResponseDto.getChannel());
        detailDto.setStatus(summaryResponseDto.getStatus());
        detailDto.setSentAt(summaryResponseDto.getSentAt());
        detailDto.setUserEmail(notification.getUserEmail());
        detailDto.setRetryCount(notification.getRetryCount());
        detailDto.setIsRead(notification.isRead());
        detailDto.setCreatedAt(notification.getCreatedAt());
        detailDto.setUpdatedAt(notification.getUpdatedAt());

        return detailDto;
    }


}

package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationHistoryResponseDto {
    private Long userId;
    private List<NotificationHistoryDto> notifications;
    private Long totalCount;
    private Boolean hasMore;
}

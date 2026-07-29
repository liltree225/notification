package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.enumeration.NotificationStatus;
import org.example.enumeration.PreferredChannel;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationSummaryResponseDto {
    private Long id;
    private Long orderId;
    private String type;
    private String subject;
    private String message;
    private PreferredChannel channel;
    private NotificationStatus status;
    private LocalDateTime sentAt;
}

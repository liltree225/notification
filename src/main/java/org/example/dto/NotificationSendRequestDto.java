package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.enumeration.Type;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationSendRequestDto {
    private Long orderId;
    private Long userId;
    private String userEmail;
    private Type type;
    private String subject;
    private String message;
    private Long totalAmount;
    private String trackingNumber;
    private String reason;
}

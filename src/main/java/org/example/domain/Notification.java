package org.example.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.enumeration.EventType;
import org.example.enumeration.NotificationStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "notification")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long orderId;
    private Long userId;
    private String userEmail;
    private EventType eventType;
    private String subject;
    private String message;
    private Long totalAmount;
    private String trackingNumber;
    private String reason;
    private boolean sent;
    @OneToMany(mappedBy = "notification", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<NotificationChannelResults> channelResults = new ArrayList<>();
    private LocalDateTime sentAt;
    private NotificationStatus status;
    private Integer retryCount = 0;
    private boolean isRead = false;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}

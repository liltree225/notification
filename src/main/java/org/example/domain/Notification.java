package org.example.domain;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.enumeration.PreferredChannel;
import org.example.enumeration.Type;
import org.example.enumeration.NotificationStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String userEmail;
    private Long orderId;
    @Enumerated(EnumType.STRING)
    private Type type;
    private String subject;
    private String message;
    @Enumerated(EnumType.STRING)
    private PreferredChannel channel;
    @Enumerated(EnumType.STRING)
    private NotificationStatus status;
    private LocalDateTime sentAt;
    private Integer retryCount = 0;
    private LocalDateTime createdAt;


}

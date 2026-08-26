package kafka.inbox;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.UUID;

// notification/src/main/java/kafka/inbox/InboxEvent.java
@Entity
@Table(name = "inbox", indexes = {
        @Index(name = "idx_inbox_processed", columnList = "is_processed,created_at"),
        @Index(name = "idx_inbox_status", columnList = "status,created_at"),
        @Index(name = "idx_inbox_idempotency", columnList = "aggregate_id,aggregate_type", unique = true)
})
@Data
@NoArgsConstructor
public class InboxEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Уникальный ID для идемпотентности

    @Column(nullable = false)
    private Long aggregateId; // order_id

    @Column(nullable = false, length = 255)
    private String aggregateType;

    @Column(nullable = false)
    private String payload;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime processedAt;

    @Column(nullable = false)
    private Boolean isProcessed = false;

    private String errorMessage;

    @Column(length = 50)
    private String status = "RECEIVED"; // RECEIVED, PROCESSED, FAILED

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}

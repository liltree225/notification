package org.example.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.enumeration.PreferredChannel;
import org.example.enumeration.Type;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification_templates",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_notification_templates_type_channel",
                        columnNames = {"type", "channel"}
                )
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Type type;
    private PreferredChannel channel;
    private String subjectTemplate;
    private String bodyTemplate;
    private LocalDateTime createdAt;

}

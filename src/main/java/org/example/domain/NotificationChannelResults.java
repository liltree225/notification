package org.example.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.enumeration.PreferredChannel;

@Entity
//@Table(name = "notification_channel_results")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationChannelResults {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    private PreferredChannel channel;
    private boolean success;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notification_id", nullable = false)
    private Notification notification;
    private String errorMessage;
}

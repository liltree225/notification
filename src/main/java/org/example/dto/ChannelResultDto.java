package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.Notification;
import org.example.enumeration.PreferedChannel;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChannelResultDto {

    private PreferedChannel channel;
    private boolean success;
    private Long notificationId;
    private String errorMessage;
}

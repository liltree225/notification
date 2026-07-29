package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.enumeration.PreferredChannel;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChannelResultDto {

    private PreferredChannel channel;
    private boolean success;
    private Long notificationId;
    private String errorMessage;
}

package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.enumeration.PreferredChannel;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPreferencesResponseDto {
    private Long userId;
    private boolean emailEnabled;
    private boolean smsEnabled;
    private boolean pushEnabled;
    private boolean telegramEnabled;
    private String telegramChatId;
    private PreferredChannel preferredChannel;
}

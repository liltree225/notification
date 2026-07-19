package org.example.mapper;

import lombok.RequiredArgsConstructor;
import org.example.domain.UserPreferences;
import org.example.dto.UserPreferencesRequestDto;
import org.example.dto.UserPreferencesResponseDto;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserPreferencesMapper {
    public UserPreferencesResponseDto toDto(UserPreferences preferences) {
        return new UserPreferencesResponseDto(preferences.getUserId(),
                preferences.isEmailEnabled(),
                preferences.isSmsEnabled(),
                preferences.isPushEnabled(),
                preferences.isTelegramEnabled(),
                preferences.getTelegramChatId(),
                preferences.getPreferedChannel());
    }

    public UserPreferences toEntity(UserPreferencesRequestDto requestDto){
        UserPreferences userPreferences = new UserPreferences();
        userPreferences.setUserId(requestDto.getUserId());
        userPreferences.setEmailEnabled(requestDto.isEmailEnabled());
        userPreferences.setSmsEnabled(requestDto.isSmsEnabled());
        userPreferences.setPushEnabled(requestDto.isPushEnabled());
        userPreferences.setTelegramEnabled(requestDto.isTelegramEnabled());
        userPreferences.setTelegramChatId(requestDto.getTelegramChatId());
        userPreferences.setPreferedChannel(requestDto.getPreferedChannel());
        return userPreferences;

    }
}

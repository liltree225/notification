package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.domain.UserPreferences;
import org.example.dto.UserPreferencesRequestDto;
import org.example.dto.UserPreferencesResponseDto;
import org.example.mapper.UserPreferencesMapper;
import org.example.repository.UserPreferencesDao;
import org.example.service.PreferencesService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class PreferencesServiceImpl implements PreferencesService {
    private final UserPreferencesMapper preferencesMapper;
    private final UserPreferencesDao userPreferencesDao;

    @Override
    public UserPreferencesResponseDto getPreferences(Long userId) {
        return preferencesMapper.toDto(userPreferencesDao.findByUserId(userId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Настройки с ID " + userId + " не найдены")));
    }

    @Override
    public UserPreferencesResponseDto postPreferences(UserPreferencesRequestDto requestDto) {
        UserPreferences preferences = userPreferencesDao.findByUserId(requestDto.getUserId())
                .orElse(new UserPreferences());

        preferences.setUserId(requestDto.getUserId());
        preferences.setEmailEnabled(requestDto.isEmailEnabled());
        preferences.setSmsEnabled(requestDto.isSmsEnabled());
        preferences.setPushEnabled(requestDto.isPushEnabled());
        preferences.setTelegramEnabled(requestDto.isTelegramEnabled());
        preferences.setPreferedChannel(requestDto.getPreferedChannel());

        if (requestDto.isTelegramEnabled()) {
            if (requestDto.getTelegramChatId() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Telegram chat ID is required");
            }
            preferences.setTelegramChatId(requestDto.getTelegramChatId());
        }

        userPreferencesDao.save(preferences);

        return preferencesMapper.toDto(preferences);
    }


}

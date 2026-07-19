package org.example.service;

import org.example.dto.UserPreferencesRequestDto;
import org.example.dto.UserPreferencesResponseDto;

public interface PreferencesService {
    public UserPreferencesResponseDto getPreferences(Long userId);
    public UserPreferencesResponseDto postPreferences(UserPreferencesRequestDto requestDto);
}

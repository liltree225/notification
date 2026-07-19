package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.UserPreferencesRequestDto;
import org.example.dto.UserPreferencesResponseDto;
import org.example.service.PreferencesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/preferences")
@RequiredArgsConstructor
public class PreferencesController {
    private final Logger logger = LoggerFactory.getLogger(PreferencesController.class);
    private final PreferencesService preferencesService;

    @GetMapping("/{userId}")
    public UserPreferencesResponseDto getPreferences(@PathVariable Long userId){
        return preferencesService.getPreferences(userId);
    }

    @PostMapping
    public UserPreferencesResponseDto postPreferences(@RequestBody UserPreferencesRequestDto requestDto){
        return preferencesService.postPreferences(requestDto);
    }



}

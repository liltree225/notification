package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.*;
import org.example.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/notifications")
@RequiredArgsConstructor

public class NotificationController {

    private final Logger logger = LoggerFactory.getLogger(NotificationController.class);
    private final NotificationService notificationService;
    @GetMapping("/hello")
    void hello(){
        System.out.println("hello");
    }

    @PostMapping("/send")
    public NotificationResponseDto sendMessage(@RequestBody NotificationSendRequestDto requestDto){
       return notificationService.sendMessage(requestDto);
    }

    @GetMapping
    public NotificationHistoryResponseDto getNotificationHistory(@RequestParam Long userId, @RequestParam(required = false) String type, @PageableDefault(size = 10, sort = "sentAt", direction = Sort.Direction.DESC)Pageable pageable){
        return notificationService.getNotificationHistory(userId, type, pageable);
    }

    @GetMapping("/{id}")
    public NotificationDetailResponseDto getNotification(@RequestParam Long id){
        return notificationService.getNotification(id);
    }

    @PatchMapping("/{id}/read")
    public NotificationDetailResponseDto notificationMarkAsRead(@RequestParam Long id){
        return notificationService.notificationMarkAsRead(id);
    }

    @PostMapping("/retry")
    public RetryResponseDto retrySend(@RequestBody RetryRequestDto requestDto){
        return notificationService.retrySend(requestDto);
    }






}

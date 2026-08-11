package kafka.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.NotificationSendRequestDto;
import org.example.service.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {
    private final NotificationService service;
    @KafkaListener(topics = "notification")
    public void listen(NotificationSendRequestDto requestDto){
        log.info("получено" + requestDto.getOrderId());
        service.sendMessage(requestDto);
    }
}

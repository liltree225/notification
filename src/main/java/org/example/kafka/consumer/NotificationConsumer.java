package org.example.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.example.kafka.inbox.InboxEvent;
import org.example.kafka.inbox.InboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.NotificationSendRequestDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {
    private final InboxEventRepository inboxEventRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "order-events", groupId = "notification-service")
    public void listen(String message,
                       @Header(KafkaHeaders.RECEIVED_KEY) String messageKey) {
        log.info("Received message from Kafka. Key: {}", messageKey);

        try {
            // Парсим payload
            NotificationSendRequestDto dto = objectMapper.readValue(
                    message,
                    NotificationSendRequestDto.class
            );

            // Проверяем идемпотентность - есть ли уже в INBOX
            Optional<InboxEvent> existing = inboxEventRepository
                    .findByAggregateIdAndType(dto.getOrderId(), "ORDER_CREATED");

            if (existing.isPresent()) {
                log.warn("Event already processed. OrderId: {}", dto.getOrderId());
                return;
            }

            // Сохраняем в INBOX для дальнейшей обработки
            InboxEvent event = new InboxEvent();
            event.setAggregateId(dto.getOrderId());
            event.setAggregateType("ORDER_CREATED");
            event.setPayload(message);
            event.setStatus("RECEIVED");

            inboxEventRepository.save(event);
            log.info("Event saved to inbox. OrderId: {}", dto.getOrderId());

        } catch (Exception e) {
            log.error("Error processing Kafka message: {}", e.getMessage(), e);

        }
    }
}


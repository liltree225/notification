package kafka.inbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.NotificationSendRequestDto;
import org.example.service.NotificationService;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class InboxProcessor {
    private final InboxEventRepository inboxEventRepository;
    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;
    private final MeterRegistry meterRegistry;

    private static final int BATCH_SIZE = 10;

    @Scheduled(fixedDelay = 3000) // каждые 3 секунды
    @Transactional
    public void processInboxEvents() {
        log.debug("Starting Inbox processing...");

        try {
            List<InboxEvent> unprocessed = inboxEventRepository
                    .findUnprocessedEvents(PageRequest.of(0, BATCH_SIZE));

            if (unprocessed.isEmpty()) {
                log.debug("No unprocessed events");
                return;
            }

            log.info("Found {} unprocessed events", unprocessed.size());

            for (InboxEvent event : unprocessed) {
                processEvent(event);
            }
        } catch (Exception e) {
            log.error("Error in processInboxEvents: {}", e.getMessage(), e);
            meterRegistry.counter("inbox.failed.total").increment();
        }
        meterRegistry.counter("inbox.processed.total").increment();
    }

    private void processEvent(InboxEvent event) {
        try {
            // Парсим payload
            NotificationSendRequestDto dto = objectMapper.readValue(
                    event.getPayload(),
                    NotificationSendRequestDto.class
            );

            // Обрабатываем бизнес-логику
            notificationService.sendMessage(dto);

            // Отмечаем как обработанное
            event.setIsProcessed(true);
            event.setStatus("PROCESSED");
            event.setProcessedAt(LocalDateTime.now());
            inboxEventRepository.save(event);

            log.info("Event processed successfully. OrderId: {}", dto.getOrderId());

        } catch (Exception e) {
            log.error("Error processing inbox event {}: {}", event.getId(), e.getMessage());
            event.setErrorMessage(e.getMessage());
            event.setStatus("FAILED");
            inboxEventRepository.save(event);
        }
    }
}


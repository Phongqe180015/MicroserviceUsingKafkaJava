package com.example.productservice.event;

import com.example.productservice.common.event.UserEventEnvelope;
import com.example.productservice.entity.ProcessedEvent;
import com.example.productservice.entity.UserReadModel;
import com.example.productservice.repository.ProcessedEventRepository;
import com.example.productservice.repository.UserReadModelRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserEventConsumer {

    private final UserReadModelRepository userReadModelRepository;
    private final ProcessedEventRepository processedEventRepository;

    @KafkaListener(topics = "${app.kafka.topic}", groupId = "product-service")
    @Transactional
    public void consume(UserEventEnvelope envelope) {
        log.info("📥 Received user event from Kafka - EventId: {}, UserId: {}, EventType: {}", 
                envelope.getEventId(), envelope.getData().getUserId(), envelope.getEventType());
        
        // 1) Idempotency check
        if (processedEventRepository.existsById(envelope.getEventId())) {
            log.info("⚠️ Event already processed, skipping - EventId: {}", envelope.getEventId());
            return;
        }

        // 2) Upsert read model
        var data = envelope.getData();
        UserReadModel rm = userReadModelRepository.findById(data.getUserId())
                .orElseGet(UserReadModel::new);

        rm.setUserId(data.getUserId());
        rm.setName(data.getName());
        rm.setEmail(data.getEmail());
        rm.setStatus(data.getStatus());
        rm.setUpdatedAt(data.getUpdateAt());

        userReadModelRepository.save(rm);

        // 3) Mark event processed
        ProcessedEvent pe = new ProcessedEvent();
        pe.setEventId(envelope.getEventId());
        processedEventRepository.save(pe);
        
        log.info("✅ User event processed successfully - UserId: {}, Name: {}, Email: {}", 
                data.getUserId(), data.getName(), data.getEmail());
    }
}

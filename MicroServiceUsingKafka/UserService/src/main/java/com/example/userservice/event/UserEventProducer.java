package com.example.userservice.event;

import com.example.userservice.common.event.UserEventEnvelope;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserEventProducer {
    private final KafkaTemplate<String, UserEventEnvelope> kafkaTemplate;

    @Value("${app.kafka.topic}")
    private String topic;
    public void publish(Long userId, UserEventEnvelope envelope){
        log.info("📤 Sending user event to Kafka - Topic: {}, UserId: {}, EventType: {}", 
            topic, userId, envelope.getEventType());
        kafkaTemplate.send(topic,String.valueOf(userId), envelope);
        log.info("✅ Event sent successfully to Kafka!");
    }
}

package com.example.userservice.common.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEventEnvelope {
    private UUID eventId;
    private String eventType;
    private Instant occuredAt;
    private UserEventData data;
}

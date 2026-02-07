package com.example.productservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "processed_events")
public class ProcessedEvent {
    @Id
    private UUID eventId;

    @Column(nullable=false)
    private Instant processedAt = Instant.now();
}

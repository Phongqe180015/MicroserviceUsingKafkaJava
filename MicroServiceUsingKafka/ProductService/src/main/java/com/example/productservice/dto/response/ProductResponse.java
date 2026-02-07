package com.example.productservice.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ProductResponse {
    private Long id;
    private String name;
    private Long price;
    private Long createdBy;
    private Instant createdAt;
}

package com.example.productservice.dto.external;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserServiceResponse {
    private Long id;
    private String name;
    private String email;
    private String status;
    private Instant createdAt;
    private Instant updatedAt;
}

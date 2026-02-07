package com.example.userservice.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private String status;
    private Instant createdAt;
    private Instant updatedAt;
}

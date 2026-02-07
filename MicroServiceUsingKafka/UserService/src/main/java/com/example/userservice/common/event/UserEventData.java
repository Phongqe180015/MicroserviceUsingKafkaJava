package com.example.userservice.common.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEventData {
    private Long userId;
    private String name;
    private String email;
    private String status;
    private Instant updateAt;
}

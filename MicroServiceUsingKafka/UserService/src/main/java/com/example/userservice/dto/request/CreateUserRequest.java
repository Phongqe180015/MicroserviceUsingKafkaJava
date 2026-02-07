package com.example.userservice.dto.request;

import lombok.Data;

@Data
public class CreateUserRequest {
    private String name;
    private String email;
}

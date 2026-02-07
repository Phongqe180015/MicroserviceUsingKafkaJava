package com.example.userservice.controller;

import com.example.userservice.dto.request.CreateUserRequest;
import com.example.userservice.dto.request.UpdateUserStatusRequest;
import com.example.userservice.dto.response.UserResponse;
import com.example.userservice.entity.User;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUser(@RequestBody CreateUserRequest req) {
        User saved = userService.createUser(req.getName(), req.getEmail());
        return toResponse(saved);
    }

    @PatchMapping("/{id}/status")
    public UserResponse updateStatus(@PathVariable Long id, @RequestBody UpdateUserStatusRequest req) {
        User saved = userService.updateStatus(id, req.getStatus());
        return toResponse(saved);
    }

    @GetMapping("/{id}")
    public UserResponse getById(@PathVariable Long id) {
        User u = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User with id " + id + " not found"));
        return toResponse(u);
    }

    private UserResponse toResponse(User u) {
        return UserResponse.builder()
                .id(u.getId())
                .name(u.getName())
                .email(u.getEmail())
                .status(u.getStatus())
                .createdAt(u.getCreatedAt())
                .updatedAt(u.getUpdatedAt())
                .build();
    }
}

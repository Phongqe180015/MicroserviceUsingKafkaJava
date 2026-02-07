package com.example.userservice.service;

import com.example.userservice.common.event.UserEventData;
import com.example.userservice.common.event.UserEventEnvelope;
import com.example.userservice.entity.User;
import com.example.userservice.event.UserEventProducer;
import com.example.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserEventProducer userEventProducer;

    public User createUser(String name, String email) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setStatus("Active");
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());
        User savedUser = userRepository.save(user);

        UserEventEnvelope env =  UserEventEnvelope.builder()
                .eventId(UUID.randomUUID())
                .eventType("UserCreated")
                .occuredAt(Instant.now())
                .data(UserEventData.builder()
                        .userId(savedUser.getId())
                        .name(savedUser.getName())
                        .email(savedUser.getEmail())
                        .status(savedUser.getStatus())
                        .updateAt(savedUser.getUpdatedAt())
                        .build())
                .build();
        userEventProducer.publish(savedUser.getId(), env);
        return savedUser;
    }
    public User updateStatus(Long userId, String status) {
        User u = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        u.setStatus(status);
        u.setUpdatedAt(Instant.now());
        User saved = userRepository.save(u);

        UserEventEnvelope env = UserEventEnvelope.builder()
                .eventId(UUID.randomUUID())
                .eventType("UserStatusUpdated")
                .occuredAt(Instant.now())
                .data(UserEventData.builder()
                        .userId(saved.getId())
                        .name(saved.getName())
                        .email(saved.getEmail())
                        .status(saved.getStatus())
                        .updateAt(saved.getUpdatedAt())
                        .build())
                .build();

        userEventProducer.publish(saved.getId(), env);
        return saved;
    }
}

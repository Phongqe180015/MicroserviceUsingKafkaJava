package com.example.productservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "user_read_model")
public class UserReadModel {
    @Id
    private Long userId;

    @Column(nullable=false, length=100)
    private String name;

    @Column(nullable=false, length=150)
    private String email;

    @Column(nullable=false, length=20)
    private String status;

    @Column(nullable=false)
    private Instant updatedAt;
}

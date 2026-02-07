package com.example.productservice.repository;

import com.example.productservice.entity.UserReadModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserReadModelRepository extends JpaRepository<UserReadModel, Long> {
}

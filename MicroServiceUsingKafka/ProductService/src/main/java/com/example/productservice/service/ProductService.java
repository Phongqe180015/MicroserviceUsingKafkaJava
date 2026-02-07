package com.example.productservice.service;

import com.example.productservice.entity.Product;
import com.example.productservice.repository.ProductRepository;
import com.example.productservice.repository.UserReadModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final UserReadModelRepository userReadModelRepository;

    public Product createProduct(String name, long price, long createdBy) {
        var user = userReadModelRepository.findById(createdBy)
                .orElseThrow(() -> new IllegalStateException("USER_NOT_SYNCED"));

        if ("BANNED".equalsIgnoreCase(user.getStatus())) {
            throw new SecurityException("USER_BANNED");
        }

        Product p = new Product();
        p.setName(name);
        p.setPrice(price);
        p.setCreatedBy(createdBy);
        return productRepository.save(p);
    }
}

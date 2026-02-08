package com.example.productservice.controller;

import com.example.productservice.client.UserServiceClient;
import com.example.productservice.dto.external.UserServiceResponse;
import com.example.productservice.dto.request.CreateProductRequest;
import com.example.productservice.dto.response.ProductResponse;
import com.example.productservice.entity.Product;
import com.example.productservice.repository.ProductRepository;
import com.example.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final ProductRepository productRepository;
    private final UserServiceClient userServiceClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductResponse create(@RequestBody CreateProductRequest req) {
        Product saved = productService.createProduct(req.getName(), req.getPrice(), req.getCreatedBy());
        return toResponse(saved);
    }

    @GetMapping
    public List<ProductResponse> list(@RequestParam(required = false) Long createdBy) {
        List<Product> items = (createdBy == null)
                ? productRepository.findAll()
                : productRepository.findByCreatedBy(createdBy);

        return items.stream().map(this::toResponse).toList();
    }

    /**
     * 🌐 DEMO HTTP CALL: Lấy thông tin user từ UserService qua HTTP
     */
    @GetMapping("/user/{userId}/info")
    public Map<String, Object> getUserInfoViaHttp(@PathVariable Long userId) {
        UserServiceResponse user = userServiceClient.getUserById(userId);

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "User not found or UserService unavailable");
        }

        return Map.of(
                "message", "✅ Successfully fetched user via HTTP",
                "source", "UserService HTTP API",
                "user", user
        );
    }

    /**
     * 🌐 DEMO HTTP CALL: Tạo product với validation qua HTTP call (không dùng read model)
     */
    @PostMapping("/http")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> createProductViaHttp(@RequestBody CreateProductRequest req) {
        // Validate user qua HTTP thay vì dùng read model
        UserServiceResponse user = userServiceClient.getUserById(req.getCreatedBy());

        if (user == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "User not found via HTTP call to UserService");
        }

        if ("BANNED".equalsIgnoreCase(user.getStatus())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "User is banned - verified via HTTP");
        }

        // Tạo product (không thông qua ProductService để tránh check read model)
        Product product = new Product();
        product.setName(req.getName());
        product.setPrice(req.getPrice());
        product.setCreatedBy(req.getCreatedBy());
        Product saved = productRepository.save(product);

        return Map.of(
                "message", "✅ Product created with HTTP validation",
                "validation", "User status checked via HTTP call to UserService",
                "product", toResponse(saved),
                "validatedUser", Map.of(
                        "id", user.getId(),
                        "name", user.getName(),
                        "status", user.getStatus()
                )
        );
    }

    private ProductResponse toResponse(Product p) {
        return ProductResponse.builder()
                .id(p.getId())
                .name(p.getName())
                .price(p.getPrice())
                .createdBy(p.getCreatedBy())
                .createdAt(p.getCreatedAt())
                .build();
    }
}

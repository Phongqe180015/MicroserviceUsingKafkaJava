package com.example.productservice.controller;

import com.example.productservice.dto.request.CreateProductRequest;
import com.example.productservice.dto.response.ProductResponse;
import com.example.productservice.entity.Product;
import com.example.productservice.repository.ProductRepository;
import com.example.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final ProductRepository productRepository;

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

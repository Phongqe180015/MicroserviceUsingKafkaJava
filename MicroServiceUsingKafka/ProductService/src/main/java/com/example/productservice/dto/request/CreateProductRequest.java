package com.example.productservice.dto.request;


import lombok.Data;

@Data
public class CreateProductRequest {
    private String name;
    private Long price;
    private Long createdBy;
}

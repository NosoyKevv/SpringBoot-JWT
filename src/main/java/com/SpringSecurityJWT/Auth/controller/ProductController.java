package com.SpringSecurityJWT.Auth.controller;

import com.SpringSecurityJWT.Auth.entity.Product;
import com.SpringSecurityJWT.Auth.repository.ProductRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productRepository.findAll();

        if (products != null && !products.isEmpty()) {

            return ResponseEntity.ok(products);
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productRepository.save(product));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleException(Exception ex, HttpServletRequest request) {
        Map<String, String> apiError = new HashMap<>();
        apiError.put("message", ex.getMessage());
        apiError.put("timestamp", new Date().toString());
        apiError.put("url", request.getRequestURI());
        apiError.put("method", request.getMethod());

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        if (ex instanceof AccessDeniedException) {
            status = HttpStatus.FORBIDDEN;
        }
        return ResponseEntity.status(status).body(apiError);
    }
}
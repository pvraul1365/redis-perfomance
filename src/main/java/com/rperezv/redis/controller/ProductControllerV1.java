package com.rperezv.redis.controller;

import com.rperezv.redis.entity.Product;
import com.rperezv.redis.service.ProductServiceV1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * ProductController
 * <p>
 * Created by IntelliJ, Spring Framework Guru.
 *
 * @author architecture - pvraul
 * @version 13/09/2025 - 15:52
 * @since 1.17
 */
@RestController
@RequestMapping("product/v1")
@RequiredArgsConstructor
@Slf4j
public class ProductControllerV1 {

    private final ProductServiceV1 service;

    @GetMapping("{id}")
    public Mono<Product> getProductById(@PathVariable Integer id) {
        log.info("Getting product (in controller) by id: {}", id);

        return this.service.getProductById(id);
    }

    @PutMapping("{id}")
    public Mono<Product> updateProductById(@PathVariable Integer id, @RequestBody Mono<Product> product) {
        log.info("Updating product (in controller) by id: {}", id);

        return this.service.updateProduct(id, product);
    }
}

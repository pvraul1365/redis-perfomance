package com.rperezv.redis.service;

import com.rperezv.redis.entity.Product;
import com.rperezv.redis.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * ProductService
 * <p>
 * Created by IntelliJ, Spring Framework Guru.
 *
 * @author architecture - pvraul
 * @version 13/09/2025 - 15:47
 * @since 1.17
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository repository;

    public Mono<Product> getProductById(Integer id) {
        log.info("Get product (in service) by id: {}", id);

        return this.repository.findById(id);
    }

    public Mono<Product> updateProduct(Integer id, Mono<Product> product) {
        log.info("Update product (in service) by id: {}", product);

        return this.repository.findById(id)
                .flatMap(p -> product.doOnNext(pr -> pr.setId(id)))
                .flatMap(this.repository::save);
    }

}

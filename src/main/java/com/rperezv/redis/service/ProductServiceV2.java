package com.rperezv.redis.service;

import com.rperezv.redis.entity.Product;
import com.rperezv.redis.service.util.CacheTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * ProductServiceV2
 * <p>
 * Created by IntelliJ, Spring Framework Guru.
 *
 * @author architecture - pvraul
 * @version 21/09/2025 - 14:42
 * @since 1.17
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceV2 {

    private final CacheTemplate<Integer, Product> cacheTemplate;

    // GET
    public Mono<Product> getProductById(Integer id) {
        log.info("Get product (in service) by id: {}", id);

        return this.cacheTemplate.get(id);
    }

    // PUT
    public Mono<Product> updateProduct(Integer id, Mono<Product> product) {
        log.info("Update product (in service) by id: {}", product);

        return product
                .flatMap(p -> this.cacheTemplate.update(id, p));
    }

    // DELETE
    public Mono<Void> deleteProduct(Integer id) {
        log.info("Delete product (in service) by id: {}", id);

        return this.cacheTemplate.delete(id);
    }

}

package com.rperezv.redis.service.util;

import com.rperezv.redis.entity.Product;
import com.rperezv.redis.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMapReactive;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.codec.TypedJsonJacksonCodec;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * ProductCacheTemplate
 * <p>
 * Created by IntelliJ, Spring Framework Guru.
 *
 * @author architecture - pvraul
 * @version 27/09/2025 - 09:09
 * @since 1.17
 */
@Service
@Slf4j
public class ProductCacheTemplate extends CacheTemplate<Integer, Product> {

    private final ProductRepository repository;
    private RMapReactive<Integer, Product> map;

    public ProductCacheTemplate(ProductRepository repository, RedissonReactiveClient client) {
        this.repository = repository;
        this.map = client.getMap("prodcut", new TypedJsonJacksonCodec(Integer.class, Product.class));
    }

    @Override
    protected Mono<Product> getFromSource(Integer id) {
        // Implementation to get Product from the primary data source (e.g., database)
        return this.repository.findById(id);
    }

    @Override
    protected Mono<Product> getFromCache(Integer id) {
        // Implementation to get Product from the cache (e.g., Redis)
        return this.map.get(id);
    }

    @Override
    protected Mono<Product> updateSource(Integer id, Product product) {
        // Implementation to update Product in the primary data source (e.g., database)
        return this.repository.findById(id)
                .doOnNext(p -> product.setId(id))
                .flatMap(p -> this.repository.save(product));
    }

    @Override
    protected Mono<Product> updateCache(Integer id, Product product) {
        // Implementation to update Product in the cache (e.g., Redis)
        return this.map.fastPut(id, product).thenReturn(product);
    }

    @Override
    protected Mono<Void> deleteFromSource(Integer id) {
        // Implementation to delete Product from the primary data source (e.g., database)
        return this.repository.deleteById(id);
    }

    @Override
    protected Mono<Void> deleteFromCache(Integer id) {
        // Implementation to delete Product from the cache (e.g., Redis)
        return this.map.fastRemove(id).then();
    }
}

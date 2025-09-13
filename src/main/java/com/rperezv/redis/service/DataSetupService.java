package com.rperezv.redis.service;

import com.rperezv.redis.entity.Product;
import com.rperezv.redis.repository.ProductRepository;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * DataSetupService
 * <p>
 * Created by IntelliJ, Spring Framework Guru.
 *
 * @author architecture - pvraul
 * @version 13/09/2025 - 16:11
 * @since 1.17
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DataSetupService implements CommandLineRunner {

    private final ProductRepository repository;

    private final R2dbcEntityTemplate template;

    @Value("classpath:schema.sql")
    private Resource resource;

    @Override
    public void run(String... args) throws Exception {
        String query = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        log.info(query);

        Mono<Void> insert = Flux.range(1, 1000)
                .map(i -> Product.builder()
                        .id(null)
                        .description("product" + i)
                        .price(ThreadLocalRandom.current().nextDouble(1, 100))
                        .build()
                )
                .collectList()
                .flatMapMany(this.repository::saveAll)
                .then();

        this.template.getDatabaseClient()
                .sql(query)
                .then()
                .then(insert)
                .doFinally(s -> log.info("Database initialized {}", s))
                .subscribe();

    }

}

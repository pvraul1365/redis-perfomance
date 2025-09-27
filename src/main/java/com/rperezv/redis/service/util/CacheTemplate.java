package com.rperezv.redis.service.util;

import reactor.core.publisher.Mono;

/**
 * CacheService
 * <p>
 * Created by IntelliJ, Spring Framework Guru.
 *
 * @author architecture - pvraul
 * @version 27/09/2025 - 08:56
 * @since 1.17
 */
public abstract class CacheTemplate<KEY, ENTITY> {

    public Mono<ENTITY> get(KEY key) {
        return getFromCache(key)
                .switchIfEmpty(
                        getFromSource(key)
                                .flatMap(entity -> updateCache(key, entity))
                );
    }

    public Mono<ENTITY> update(KEY key, ENTITY entity) {
        return updateSource(key, entity)
                .flatMap(updatedEntity -> deleteFromCache(key).thenReturn(updatedEntity));
    }

    public Mono<Void> delete(KEY key) {
        return deleteFromSource(key)
                .then(deleteFromCache(key));
    }

    abstract protected Mono<ENTITY> getFromSource(KEY key);
    abstract protected Mono<ENTITY> getFromCache(KEY key);
    abstract protected Mono<ENTITY> updateSource(KEY key, ENTITY entity);
    abstract protected Mono<ENTITY> updateCache(KEY key, ENTITY entity);
    abstract protected Mono<Void> deleteFromSource(KEY key);
    abstract protected Mono<Void> deleteFromCache(KEY key);

}

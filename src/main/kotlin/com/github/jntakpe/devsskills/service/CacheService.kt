package com.github.jntakpe.devsskills.service

import com.github.jntakpe.devsskills.model.Identifiable
import org.slf4j.LoggerFactory
import org.springframework.cache.Cache
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.core.publisher.toMono
import kotlin.reflect.KProperty1

@Service
class CacheService(private val cacheManager: RedisCacheManager) {

    val logger = LoggerFactory.getLogger(javaClass.simpleName)

    fun <T : Identifiable> store(value: T, property: KProperty1<T, *>, key: String? = null): T {
        logger.info("Storing {} in cache with key {}", value.toString(), key ?: value.id)
        resolveCache(value.javaClass, property).put(key ?: value.id.toString(), value)
        return value
    }

    fun <T : Identifiable> retrieve(key: String, type: Class<T>, property: KProperty1<T, *>): Mono<T> {
        logger.debug("Retrieving cached key with ${property.name} and key : $key")
        val cache = resolveCache(type, property)
        return (cache.get(key, type)?.toMono() ?: cacheMiss(cache, key))
                .doOnNext { logger.debug("{} retrieved from cache {} with key {}", it, cache.name, key) }
    }

    private fun <T : Identifiable> cacheMiss(cache: Cache, key: String): Mono<T> {
        logger.debug("Cache miss from cache {} with key {}", cache.name, key)
        return Mono.empty()
    }

    private fun <T : Identifiable> resolveCache(clazz: Class<T>, property: KProperty1<T, *>): Cache {
        val cacheName = "${clazz.simpleName.toLowerCase()}_${property.name}"
        return cacheManager.getCache(cacheName) ?: throw IllegalStateException("Cache $cacheName not configured")
    }
}
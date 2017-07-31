package com.github.jntakpe.devsskills.config

import com.github.jntakpe.devsskills.config.properties.RedisCacheProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.cache.RedisCacheWriter
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import java.time.Duration
import java.time.Duration.ofHours
import java.time.Duration.ofSeconds

@Configuration
class CacheConfig(private val cacheProperties: RedisCacheProperties) {

    @Bean
    fun redisConnectionFactory() = LettuceConnectionFactory(RedisStandaloneConfiguration("localhost", 6379),
            LettuceClientConfiguration.builder().shutdownTimeout(ofSeconds(1)).build())

    @Bean
    fun cacheManager() = RedisCacheManager(redisCacheWriter(), defaultCacheConfig(), buildCachesConfig())

    private fun defaultCacheConfig(duration: Duration = ofHours(1)) = RedisCacheConfiguration.defaultCacheConfig().entryTtl(duration)

    private fun redisCacheWriter() = RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory())

    private fun buildCachesConfig() = cacheProperties.caches.associateBy({ it.name }, { defaultCacheConfig(ofSeconds(it.expiry)) })

}
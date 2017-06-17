package com.github.jntakpe.devsskills.config

import com.github.jntakpe.devsskills.config.properties.RedisCacheProperties
import com.github.jntakpe.devsskills.config.properties.RedisCacheProperties.Companion.DEFAULT_EXPIRY
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import java.time.Duration

@Configuration
class CacheConfig(private val cacheProperties: RedisCacheProperties) {

    @Bean
    fun redisConnectionFactory() = LettuceConnectionFactory(RedisStandaloneConfiguration("localhost", 6379),
            LettuceClientConfiguration.builder().shutdownTimeout(Duration.ofSeconds(1)).build())

    @Bean
    fun redisTemplate() = RedisTemplate<String, Any>().apply { connectionFactory = redisConnectionFactory() }

    @Bean
    fun cacheManager() = RedisCacheManager(redisTemplate()).apply { configureCacheManager() }

    private fun RedisCacheManager.configureCacheManager() {
        setDefaultExpiration(DEFAULT_EXPIRY)
        setUsePrefix(true)
        cacheNames = cacheProperties.caches.map(RedisCacheProperties.RedisCache::name)
        setExpires(cacheProperties.caches.associateBy({ it.name }, { it.expiry }))
    }

}
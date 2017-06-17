package com.github.jntakpe.devsskills.config.properties

//FIXME when https://github.com/spring-projects/spring-boot/issues/4563 use kotlin

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import java.time.Duration
import java.util.*

@Component
@ConfigurationProperties("redis")
class RedisCacheProperties {

    var caches: List<RedisCache> = ArrayList()

    open class RedisCache {

        var name: String? = null

        var expiry = DEFAULT_EXPIRY
    }

    companion object {
        var DEFAULT_EXPIRY = Duration.ofMinutes(60).toMillis()
    }

}

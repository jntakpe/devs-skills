package com.github.jntakpe.devsskills.config

import com.github.jntakpe.devsskills.web.EmployeeHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.router
import java.net.URI

@Configuration
class RouteConfig(val employeeHandler: EmployeeHandler) {

    @Bean
    fun apiRouter() = router {
        "/api".and(accept(MediaType.APPLICATION_JSON)).nest {
            "/employees".nest {
                GET("/login/{login}", employeeHandler::findByLogin)
            }
        }
    }

    @Bean
    fun staticRouter() = router {
        accept(MediaType.TEXT_HTML).nest { GET("/", { ServerResponse.temporaryRedirect(URI.create("html5/index.html")).build() }) }
        resources("/**", ClassPathResource("static/"))
    }

}
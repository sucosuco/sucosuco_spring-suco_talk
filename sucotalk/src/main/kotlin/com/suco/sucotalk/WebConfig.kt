package com.suco.sucotalk

import com.suco.sucotalk.auth.controller.LoginMemberArgumentResolver
import com.suco.sucotalk.auth.infrastructure.LoginInterceptor
import org.springframework.context.annotation.Configuration
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig(private val loginInterceptor: LoginInterceptor, private val loginMemberArgumentResolver: LoginMemberArgumentResolver) : WebMvcConfigurer {

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOrigins("http://localhost:8080", "http://localhost:3000")
            .allowCredentials(true)
            .exposedHeaders("Authorization")
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(loginInterceptor).addPathPatterns("**/auth")
    }

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(loginMemberArgumentResolver)
    }
}
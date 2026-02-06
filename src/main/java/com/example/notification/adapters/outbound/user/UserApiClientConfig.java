package com.example.notification.adapters.outbound.user;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserApiClientConfig {

    @Bean
    public RequestInterceptor userClientInterceptor(@Value("${auth.service.x-ms-token}") String token) {
        return template -> template.header("X-MS-Token", token);
    }
}

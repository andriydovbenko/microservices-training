package com.cursor.moviecatalogservice.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class RestTemplateConfig {

    @Bean
    @LoadBalanced
    public WebClient.Builder getWebClientBuilder(){
        return WebClient.builder();
    }
}
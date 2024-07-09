package com.ags.orchestrator.service.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    @Qualifier("order")
    public WebClient orderClient(@Value("${service.endpoints.order}") String endpoint) {
        return WebClient.builder()
                .baseUrl(endpoint)
                .build();
    }

    @Bean
    @Qualifier("payment")
    public WebClient paymentClient(@Value("${service.endpoints.payment}") String endpoint) {
        return WebClient.builder()
                .baseUrl(endpoint)
                .build();
    }

    @Bean
    @Qualifier("inventory")
    public WebClient inventoryClient(@Value("${service.endpoints.product}") String endpoint) {
        return WebClient.builder()
                .baseUrl(endpoint)
                .build();
    }
}

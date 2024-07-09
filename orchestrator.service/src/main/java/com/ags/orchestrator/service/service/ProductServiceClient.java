package com.ags.orchestrator.service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.springframework.beans.factory.annotation.Value;

@Service
public class ProductServiceClient {
    @Autowired
    @Qualifier("inventory")
    private WebClient webClient;


    public ProductServiceClient(@Value("${service.endpoints.product}") String productServiceUrl) {
        this.webClient = WebClient.builder().baseUrl(productServiceUrl).build();
    }

    public Mono<Boolean> validateProduct(Long productId, Integer quantity) {
        return webClient.get()
                .uri("/products/{id}/validate?quantity={quantity}", productId, quantity)
                .retrieve()
                .bodyToMono(Boolean.class);
    }


}

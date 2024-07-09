package com.ags.orchestrator.service.service;

import com.ags.orchestrator.service.dto.OrderItemUpdateDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.springframework.beans.factory.annotation.Value;

@Service
@Slf4j
public class OrderServiceClient {
    @Autowired
    @Qualifier("order")
    private WebClient webClient;


    public OrderServiceClient(@Value("${service.endpoints.order}") String orderServiceUrl) {
        this.webClient = WebClient.builder().baseUrl(orderServiceUrl).build();
    }

    public Mono<OrderItemUpdateDTO> updateOrderItem(final OrderItemUpdateDTO orderItemUpdateDTO) {
        String uriUpdateOrderItem = String.format("/{orderId}/item/{itemId}", orderItemUpdateDTO.getProductId ());
        return this.webClient
                .put()
                .uri(uriUpdateOrderItem)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(orderItemUpdateDTO)
                .retrieve()
                .bodyToMono(OrderItemUpdateDTO.class)
                .doOnNext(orderItemUpdateDTO1 -> {
                        log.info("call {}, response", uriUpdateOrderItem, orderItemUpdateDTO);

            });
    }

}
//    public Mono<Orders> updateOrderStatus(Long orderId, OrderStatus status) {
//        return webClient.put()
//                .uri("/orders/{id}/status", orderId)
//                .bodyValue(status)
//                .retrieve()
//                .bodyToMono(Orders.class);
//    }

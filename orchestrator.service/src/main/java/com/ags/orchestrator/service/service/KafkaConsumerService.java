//package com.ags.orchestrator.service.service;
//
//import com.ags.orchestrator.service.dto.OrderItemReqDTO;
//import com.ags.orchestrator.service.status.OrderStatus;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Service;
//import org.springframework.web.reactive.function.client.WebClient;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//@KafkaListener(topics = "order-topic", groupId = "group_id")
//@Service
//@Slf4j
//public class KafkaConsumerService {
//    private final ProductServiceClient productServiceClient;
//
//    public KafkaConsumerService(ProductServiceClient productServiceClient, OrderServiceClient orderServiceClient) {
//            this.productServiceClient = productServiceClient;
//    }
//
//        @KafkaListener(topics = "order-topic", groupId = "group_id")
//        public void consumeOrder(OrderItemReqDTO orderItemReqDTO) {
//            log.info("Received order: {}", orderItemReqDTO);
//
//            // Process the received order item
//            productServiceClient.validateProduct(orderItemReqDTO.getProduct_id(), orderItemReqDTO.getQuantity())
//                    .subscribe(
//                            updatedOrder -> log.info("Updated order {} status", updatedOrder.getClass()),
//                            error -> log.error("Error processing order: {}", error.getMessage())
//                    );
//        }
//    }
//
//
////import com.ags.orchestrator.service.dto.OrderItemReqDTO;
////import com.ags.orchestrator.service.dto.OrderResponseDTO;
////import com.ags.orchestrator.service.status.OrderStatus;
////import org.springframework.kafka.annotation.KafkaListener;
////import org.springframework.stereotype.Service;
////import lombok.extern.slf4j.Slf4j;
////import reactor.core.publisher.Flux;
////import reactor.core.publisher.Mono;
////
////@Service
////@Slf4j
////public class KafkaConsumerService {
////
////    private final ProductServiceClient productServiceClient;
////    private final OrderServiceClient orderServiceClient;
////
////    public KafkaConsumerService(ProductServiceClient productServiceClient, OrderServiceClient orderServiceClient) {
////        this.productServiceClient = productServiceClient;
////        this.orderServiceClient = orderServiceClient;
////    }
////
////    @KafkaListener(topics = "order-topic", groupId = "group_id")
////    public void consumeOrder( OrderItemReqDTO orderDTO) {
////        // Process the received order DTO
////        System.out.println("Received order: " + orderDTO.toString());
////        // Implement your business logic here
////    }
//////    @KafkaListener(topics = "${KAFKA_CONSUMER_TOPIC}", groupId = "${spring.kafka.consumer.group-id}")
//////    public void consumeOrder(OrderResponseDTO order) {
//////        log.info("Received order from Kafka: {}", order.getOrder_id());
//////        processOrder(order);
//////    }
////
////    private void processOrder(OrderResponseDTO order) {
////        Flux.fromIterable(order.getOrderItems())
////                .flatMap(item -> productServiceClient.validateProduct(item.getProduct_id(), item.getQuantity())
////                        .flatMap(validationResult -> {
////                            if (validationResult) {
////                                log.info("Product {} is valid for order {}", item.getProduct_id(), order.getOrder_id());
////                                return Mono.just(true);
////                            } else {
////                                log.warn("Product {} is invalid or out of stock for order {}", item.getProduct_id(), order.getOrder_id());
////                                return orderServiceClient.updateOrderStatus(order.getOrder_id(), OrderStatus.FAILED)
////                                        .then(Mono.just(false));
////                            }
////                        })
////                )
////                .collectList()
////                .subscribe(
////                        results -> {
////                            if (results.stream().allMatch(r -> r)) {
////                                log.info("All products are valid for order {}", order.getOrder_id());
////                                orderServiceClient.updateOrderStatus(order.getOrder_id(), OrderStatus.COMPLETED)
////                                        .subscribe(
////                                                updatedOrder -> log.info("Updated order {} status to CONFIRMED", updatedOrder.getId()),
////                                                error -> log.error("Error updating order status: {}", error.getMessage())
////                                        );
////                            }
////                        },
////                        error -> log.error("Error processing order: {}", error.getMessage())
////                );
////    }
////}

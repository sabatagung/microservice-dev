package com.ags.orchestrator.service.service;

import com.ags.orchestrator.service.dto.OrderReqDTO;
import com.ags.orchestrator.service.io.OrderItem;
import com.ags.orchestrator.service.io.OrderResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class OrchestratorService {
    @Autowired
    OrderServiceClient orderServiceClient;

    @Autowired
    ProductServiceClient productServiceClient;

    @Component
    public static class OrderKafkaConsumer {

        private static final String TOPIC = "order-topic";

        @KafkaListener(topics = TOPIC, groupId = "orchestrator-group")
        public void receiveOrder(String message) {
            System.out.println("Received order: " + message);

            // Process the received order message
            // You can deserialize the message and perform necessary actions

//            private Mono<OrderResponseDTO> createOrder(final OrderItem orderItem){
//                .flatMap
//            }

        }

    }

}

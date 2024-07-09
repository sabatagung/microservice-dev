package com.ags.order.service.configuration;

import com.ags.order.service.dto1.OrderReqDTO;
import com.ags.order.service.dto1.OrderResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@EnableKafka
@Configuration
@Slf4j
public class OrderKafkaProducer {

    private static final String TOPIC = "order-topic";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public void sendOrder(OrderResponseDTO orderResponseDTO) {
        try {
            String orderJson = objectMapper.writeValueAsString(orderResponseDTO);
            kafkaTemplate.send(TOPIC, orderJson);
            log.info("Sent order JSON to Kafka: {}", orderJson);
        } catch (JsonProcessingException e) {
            log.error("Error converting order to JSON", e);
        }
    }
}

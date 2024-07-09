//package com.ags.order.service.kafka;
//
//import com.ags.order.service.dto1.OrderItemReqDTO;
//import com.ags.order.service.dto1.OrderResponseDTO;
//import org.springframework.context.annotation.Bean;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.kafka.support.SendResult;
//import org.springframework.stereotype.Service;
//import org.springframework.beans.factory.annotation.Value;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.util.concurrent.ListenableFuture;
//import org.springframework.util.concurrent.ListenableFutureCallback;
//
//import java.util.concurrent.CompletableFuture;
//
//@Service
//@Slf4j
//public class KafkaProducerService {
//
//    private final KafkaTemplate<String, OrderItemReqDTO> kafkaTemplate;
//    private final String topicName = "order-topic"; // Hardcoded topic name
//
//
//    public KafkaProducerService(KafkaTemplate<String, OrderItemReqDTO> kafkaTemplate) {
//        this.kafkaTemplate = kafkaTemplate;
//    }
//
//    public void sendOrderMessage(OrderItemReqDTO orderItemReqDTO) {
//        ListenableFuture<SendResult<String, OrderItemReqDTO>> future = (ListenableFuture<SendResult<String, OrderItemReqDTO>>) kafkaTemplate.send(topicName, orderItemReqDTO);
//
//        future.addCallback(new ListenableFutureCallback<SendResult<String, OrderItemReqDTO>>() {
//            @Override
//            public void onSuccess(SendResult<String, OrderItemReqDTO> result) {
//                log.info("Message sent successfully: {}", orderItemReqDTO.getPrice());
//                // Handle success if needed
//            }
//
//            @Override
//            public void onFailure(Throwable ex) {
//                log.error("Failed to send message: {}", ex.getMessage());
//                // Handle failure if needed
//            }
//        });
//    }
//
////    private final KafkaTemplate<String, OrderResponseDTO> kafkaTemplate;
////    private final String topicName;
////
////    public KafkaProducerService(KafkaTemplate<String, OrderResponseDTO> kafkaTemplate,
////                                @Value("${KAFKA_PRODUCER_TOPIC}") String topicName) {
////        this.kafkaTemplate = kafkaTemplate;
////        this.topicName = topicName;
////    }
////
////    public void sendOrderMessage(OrderResponseDTO orderResponseDTO) {
////        CompletableFuture<SendResult<String, OrderResponseDTO>> future = kafkaTemplate.send(topicName, orderResponseDTO);
////
////        future.whenComplete((result, ex) -> {
////            if (ex == null) {
////                log.info("Message sent successfully: {}", orderResponseDTO.getOrder_id());
////            } else {
////                log.error("Failed to send message: {}", ex.getMessage());
////            }
////        });
////    }
//}

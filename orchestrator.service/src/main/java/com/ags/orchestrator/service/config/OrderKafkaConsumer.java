//package com.ags.orchestrator.service.config;
//
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Component;
//
//@Component
//public class OrderKafkaConsumer {
//
//    private static final String TOPIC = "order-topic";
//
//    @KafkaListener(topics = TOPIC, groupId = "orchestrator-group")
//    public void receiveOrder(String message) {
//        System.out.println("Received order: " + message);
//
//        // Process the received order message
//        // You can deserialize the message and perform necessary actions
//    }
//}

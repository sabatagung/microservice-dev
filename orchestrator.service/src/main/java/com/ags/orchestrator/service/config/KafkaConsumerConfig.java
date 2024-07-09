//package com.ags.orchestrator.service.config;
//
//import org.apache.kafka.clients.consumer.ConsumerConfig;
//import org.apache.kafka.common.serialization.StringDeserializer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
//import org.springframework.kafka.core.ConsumerFactory;
//import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Configuration
//public class KafkaConsumerConfig {
//
//    @Bean
//    public ConsumerFactory<String, String> consumerFactory() {
//        Map<String, Object> props = new HashMap<>();
//        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092");
//        props.put(ConsumerConfig.GROUP_ID_CONFIG, "orchestrator-group");
//        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
//        return new DefaultKafkaConsumerFactory<>(props);
//    }
//
//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
//        ConcurrentKafkaListenerContainerFactory<String, String> factory =
//                new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(consumerFactory());
//        return factory;
//    }
//}
//
//
////
////import com.ags.orchestrator.service.dto.OrderResponseDTO;
////import org.apache.kafka.clients.consumer.ConsumerConfig;
////import org.apache.kafka.common.serialization.StringDeserializer;
////import org.springframework.beans.factory.annotation.Value;
////import org.springframework.context.annotation.Bean;
////import org.springframework.context.annotation.Configuration;
////import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
////import org.springframework.kafka.core.ConsumerFactory;
////import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
////import org.springframework.kafka.support.serializer.JsonDeserializer;
////
////import java.util.HashMap;
////import java.util.Map;
////
////@Configuration
////public class KafkaConsumerConfig {
////
////    @Value("${spring.kafka.bootstrap-servers}")
////    private String bootstrapServers;
////
////    @Value("${spring.kafka.consumer.group-id}")
////    private String groupId;
////
////    @Bean
////    public ConsumerFactory<String, OrderResponseDTO> consumerFactory() {
////        Map<String, Object> props = new HashMap<>();
////        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
////        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
////        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
////        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
////        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
////        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(OrderResponseDTO.class));
////    }
////
////    @Bean
////    public ConcurrentKafkaListenerContainerFactory<String, OrderResponseDTO> kafkaListenerContainerFactory() {
////        ConcurrentKafkaListenerContainerFactory<String, OrderResponseDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
////        factory.setConsumerFactory(consumerFactory());
////        return factory;
////    }
////}

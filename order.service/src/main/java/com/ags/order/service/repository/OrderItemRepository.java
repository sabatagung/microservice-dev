package com.ags.order.service.repository;

import com.ags.order.service.model.OrderItem;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface OrderItemRepository extends R2dbcRepository <OrderItem, Long> {
    Flux<Object> findByOrderId(Long orderId);
}

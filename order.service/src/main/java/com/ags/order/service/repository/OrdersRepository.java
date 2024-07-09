package com.ags.order.service.repository;


import com.ags.order.service.model.Orders;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersRepository extends R2dbcRepository<Orders, Long > {
}

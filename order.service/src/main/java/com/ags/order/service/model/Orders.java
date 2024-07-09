package com.ags.order.service.model;

import com.ags.order.service.status.OrderStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("orders")
public class Orders {
    @Id
    private Long id;
    private String billing_address;
    private Long customer_id;
    private LocalDate order_date;
    private String payment_method;
    private String shipping_address;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private OrderStatus orderStatus;
    private float total_amount;
}




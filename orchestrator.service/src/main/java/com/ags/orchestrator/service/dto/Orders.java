package com.ags.orchestrator.service.dto;

import com.ags.orchestrator.service.status.OrderStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Orders {
    private Long id;
    private String billing_address;
    private Long customer_id;
    private LocalDate order_date;
    private String payment_method;
    private String shipping_address;
    private OrderStatus orderStatus;
    private float total_amount;
}

package com.ags.orchestrator.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderReqDTO {
    private Long customer_id;
    private LocalDate order_date;
    private String payment_method;
    private String billing_address;
    private String shipping_address;
    private String order_status;
    List<OrderItemReqDTO> orderItems;
}

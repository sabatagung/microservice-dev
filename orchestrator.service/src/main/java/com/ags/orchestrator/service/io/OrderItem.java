package com.ags.orchestrator.service.io;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {
    private Long id;
    private float price;
    private Long product_id;
    private Integer quantity;
    private Long orderId;
}

package com.ags.order.service.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductEvent {
    private String eventType;
    private Long productId;
    private String name;
    private float price;
    private String category;
    private Integer stockQuantity;
}

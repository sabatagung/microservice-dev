package com.ags.order.service.dto1;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemReqDTO {
    private float price;
    private Long product_id;
    private Integer quantity;
}


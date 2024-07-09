package com.ags.order.service.dto1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemUpdateDTO {
    private Float price;
    private Long productId;
    private Integer quantity;
}

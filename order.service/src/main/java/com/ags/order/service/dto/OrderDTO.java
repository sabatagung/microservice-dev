package com.ags.order.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {
    private Long id;
    private String billingAddress;
    private Long customerId;
    private Date orderDate;
    private String orderStatus;
    private String paymentMethod;
    private String shippingAddress;
    private float totalAmount;
    private List<OrderItemDTO> items;
}

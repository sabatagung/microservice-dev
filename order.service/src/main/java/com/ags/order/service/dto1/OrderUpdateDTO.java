package com.ags.order.service.dto1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderUpdateDTO {
    private Long customerId;
    private LocalDate orderDate;
    private String paymentMethod;
    private String billingAddress;
    private String shippingAddress;
}

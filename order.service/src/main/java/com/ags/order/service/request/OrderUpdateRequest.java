package com.ags.order.service.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderUpdateRequest {
    private String billingAddress;
    private String orderStatus;
    private String paymentMethod;
    private String shippingAddress;
}

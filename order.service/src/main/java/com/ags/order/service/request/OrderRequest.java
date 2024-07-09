package com.ags.order.service.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequest {
    private String billingAddress;
    private String paymentMethod;
    private String shippingAddress;
    private List<OrderItemRequest> items;
}

/*
order request
orders id
billing address
payment method
shipping address

orderItem id
-> product id
-> price
-> quantity

 */

/*
order response
orders id
billing address
payment method
shipping address

orderItem id
-> product id
-> price
-> quantity

orderItem id
-> product id
-> price
-> quantity

...

total amount
 */
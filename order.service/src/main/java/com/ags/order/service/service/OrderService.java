package com.ags.order.service.service;

import com.ags.order.service.configuration.OrderKafkaProducer;
import com.ags.order.service.dto1.*;
import com.ags.order.service.exception.OrderItemNotFoundException;
import com.ags.order.service.exception.OrderNotFoundException;
import com.ags.order.service.model.OrderItem;
import com.ags.order.service.model.Orders;
import com.ags.order.service.repository.OrderItemRepository;
import com.ags.order.service.repository.OrdersRepository;
import com.ags.order.service.status.OrderStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderService {

    @Autowired
    private OrdersRepository ordersRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private OrderKafkaProducer kafkaProducer;


    @Transactional
    public Mono<OrderResponseDTO > createOrder(OrderReqDTO orderReqDTO) {
        OrderStatus orderStatus = OrderStatus.CREATED;

        Orders order = Orders.builder()
                .billing_address(orderReqDTO.getBilling_address())
                .customer_id(orderReqDTO.getCustomer_id())
                .order_date(orderReqDTO.getOrder_date())
                .orderStatus(orderStatus) // Set the order status here
                .payment_method(orderReqDTO.getPayment_method())
                .shipping_address(orderReqDTO.getShipping_address())
                .build();

        List<OrderItem> orderItems = orderReqDTO.getOrderItems().stream()
                .map(item -> OrderItem.builder()
                        .price(item.getPrice())
                        .product_id(item.getProduct_id())
                        .quantity(item.getQuantity())
                        .build())
                .collect(Collectors.toList());

        float totalAmount = calculateTotalAmount(orderItems);
        order.setTotal_amount(totalAmount);

        return ordersRepository.save(order)
                .flatMap(savedOrder -> {
                    orderItems.forEach(item -> item.setOrderId(savedOrder.getId()));
                    return orderItemRepository.saveAll(orderItems)
                            .then(Mono.defer(() -> {
                                // Create a simple String message
                                String orderMessage = "New order created - Order ID: " + savedOrder.getId() +
                                        ", Customer ID: " + savedOrder.getCustomer_id() +
                                        ", Total Amount: " + savedOrder.getTotal_amount();

                                // Log the message being sent
                                log.info("Sending order message to Kafka: {}", orderMessage);

//                                 Send order message to Kafka
                                kafkaProducer.sendOrder(OrderResponseDTO.builder().build());

                                return Mono.just(OrderResponseDTO.builder()
                                        .order_id(savedOrder.getId())
                                        .customer_id(savedOrder.getCustomer_id())
                                        .order_date(savedOrder.getOrder_date())
                                        .payment_method(savedOrder.getPayment_method())
                                        .billing_address(savedOrder.getBilling_address())
                                        .shipping_address(savedOrder.getShipping_address())
                                        .orderStatus(orderStatus)
                                        .orderItems(orderReqDTO.getOrderItems()) // use original items
                                        .build());
                            }));
                })
                .doOnError(error -> log.error("Error saving order: {}", error.getMessage()));
    }



    public Mono<Orders> updateOrderDetails(Long orderId, OrderUpdateDTO updatedOrder) {
        return ordersRepository.findById(orderId)
                .switchIfEmpty(Mono.error(new OrderNotFoundException("Order not found with id: " + orderId)))
                .flatMap(existingOrder -> {
                    // Update only specific fields
                    if (updatedOrder.getCustomerId() != null) existingOrder.setCustomer_id(updatedOrder.getCustomerId());
                    if (updatedOrder.getOrderDate() != null) existingOrder.setOrder_date(updatedOrder.getOrderDate());
                    if (updatedOrder.getPaymentMethod() != null) existingOrder.setPayment_method(updatedOrder.getPaymentMethod());
                    if (updatedOrder.getBillingAddress() != null) existingOrder.setBilling_address(updatedOrder.getBillingAddress());
                    if (updatedOrder.getShippingAddress() != null) existingOrder.setShipping_address(updatedOrder.getShippingAddress());

                    return ordersRepository.save(existingOrder);
                });
    }

    public Mono<OrderItem> updateOrderItem(Long orderId, Long itemId, OrderItemUpdateDTO updatedItemDTO) {
        return orderItemRepository.findById(itemId)
                .filter(item -> item.getOrderId().equals(orderId))
                .switchIfEmpty(Mono.error(new OrderItemNotFoundException("Order item not found with id: " + itemId + " in order: " + orderId)))
                .flatMap(existingItem -> {
                    if (updatedItemDTO.getPrice() != null) existingItem.setPrice(updatedItemDTO.getPrice());
                    if (updatedItemDTO.getProductId() != null) existingItem.setProduct_id(updatedItemDTO.getProductId());
                    if (updatedItemDTO.getQuantity() != null) existingItem.setQuantity(updatedItemDTO.getQuantity());
                    return orderItemRepository.save(existingItem);
                })
                .flatMap(savedItem -> updateOrderTotalAmount(orderId).thenReturn(savedItem));
    }

    private Mono<Orders> updateOrderTotalAmount(Long orderId) {
        return orderItemRepository.findByOrderId(orderId)
                .collectList()
                .flatMap(orderItems -> {
                    List<OrderItem> typedOrderItems = new ArrayList<>();
                    for (Object item : orderItems) {
                        if (item instanceof OrderItem) {
                            typedOrderItems.add((OrderItem) item);
                        }
                    }
                    float totalAmount = calculateTotalAmount(typedOrderItems);
                    return ordersRepository.findById(orderId)
                            .flatMap(order -> {
                                order.setTotal_amount(totalAmount);
                                return ordersRepository.save(order);
                            });
                });
    }

    public Mono<Void> deleteOrderItem(Long orderId, Long itemId) {
        return orderItemRepository.findById(itemId)
                .filter(item -> item.getOrderId().equals(orderId))
                .switchIfEmpty(Mono.error(new OrderItemNotFoundException("Order item not found with id: " + itemId + " in order: " + orderId)))
                .flatMap(orderItemRepository::delete);
    }

    public Flux<Orders> getAllOrders() {
        return ordersRepository.findAll();
    }


    private float calculateTotalAmount(List<OrderItem> orderItems) {
        return (float) orderItems.stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();
    }






//        return ordersRepository.save(order)
//                .flatMap(savedOrder -> {
//                    orderItems.forEach(item -> item.setOrderId(savedOrder.getId()));
//                    return orderItemRepository.saveAll(orderItems)
//
//                            .then(Mono.just(OrderResponseDTO.builder()
//                                    .order_id(savedOrder.getId())
//                                    .customer_id(savedOrder.getCustomer_id())
//                                    .order_date(savedOrder.getOrder_date())
//                                    .payment_method(savedOrder.getPayment_method())
//                                    .billing_address(savedOrder.getBilling_address())
//                                    .shipping_address(savedOrder.getShipping_address())
//                                    .orderStatus(orderStatus)
//                                    .orderItems(orderReqDTO.getOrderItems()) // use original items
//                                    .build()));
//                })
//                .doOnError(error -> log.error("Error saving order: {}", error.getMessage()));

//    public void sendOrderItemToKafka(OrderItemReqDTO orderItemReqDTO) {
//        kafkaProducerService.sendOrderMessage(orderItemReqDTO);
//    }


}

//    public Mono<OrderItem> updateOrderItem(Long orderId, Long itemId, OrderItemReqDTO updatedItem) {
//        return orderItemRepository.findById(itemId)
//                .flatMap(existingItem -> {
//                    if (existingItem == null || !existingItem.getOrderId().equals(orderId)) {
//                        throw new RuntimeException("Order item not found with id: " + itemId + " in order: " + orderId);
//                    }
//
//                    // Update only the fields that are present in updatedItem
//                    modelMapper.map(updatedItem, existingItem);
//
//                    // Save the updated item
//                    return orderItemRepository.save(existingItem);
//                });
//    }

//public Mono<Orders> updateOrderDetails(Long orderId, OrderReqDTO updatedOrder) {
//    return ordersRepository.findById(orderId)
//            .flatMap(existingOrder -> {
//                if (existingOrder == null) {
//                    throw new RuntimeException("Order not found with id: " + orderId);
//                }
//
//                // Use ModelMapper to map non-null fields from updatedOrder to existingOrder
//                modelMapper.map(updatedOrder, existingOrder);
//
//                // Save the updated order
//                return ordersRepository.save(existingOrder);
//            });
//}
//public Mono<Orders> updateOrderDetails(Long orderId, OrderReqDTO updatedOrder) {
//    return ordersRepository.findById(orderId)
//            .switchIfEmpty(Mono.error(new OrderNotFoundException("Order not found with id: " + orderId)))
//            .flatMap(existingOrder -> {
//                modelMapper.map(updatedOrder, existingOrder);
//                return ordersRepository.save(existingOrder);
//            });
//}

//    public Mono<OrderItem> updateOrderItem(Long orderId, Long itemId, OrderItemUpdateDTO updatedItem) {
//        return orderItemRepository.findById(itemId)
//                .filter(item -> item.getOrderId().equals(orderId))
//                .switchIfEmpty(Mono.error(new OrderItemNotFoundException("Order item not found with id: " + itemId + " in order: " + orderId)))
//                .flatMap(existingItem -> {
//                    if (updatedItem.getPrice() != null) existingItem.setPrice(updatedItem.getPrice());
//                    if (updatedItem.getProductId() != null) existingItem.setProduct_id(updatedItem.getProductId());
//                    if (updatedItem.getQuantity() != null) existingItem.setQuantity(updatedItem.getQuantity());
//
//                    return orderItemRepository.save(existingItem);
//                });
//    }

//    public Mono<OrderItem> updateOrderItem(Long orderId, Long itemId, OrderItemReqDTO updatedItem) {
//        return orderItemRepository.findById(itemId)
//                .filter(item -> item.getOrderId().equals(orderId))
//                .switchIfEmpty(Mono.error(new OrderItemNotFoundException("Order item not found with id: " + itemId + " in order: " + orderId)))
//                .flatMap(existingItem -> {
//                    modelMapper.map(updatedItem, existingItem);
//                    return orderItemRepository.save(existingItem);
//                });
//    }

//    public Mono<Void> deleteOrderItem(Long orderId, Long itemId) {
//        return orderItemRepository.findById(itemId)
//                .flatMap(existingItem -> {
//                    if (existingItem == null || !existingItem.getOrderId().equals(orderId)) {
//                        throw new RuntimeException("Order item not found with id: " + itemId + " in order: " + orderId);
//                    }
//
//                    return orderItemRepository.delete(existingItem);
//                });
//    }



//    public Mono<Orders> updateOrderByOrderId(Long orderId, OrderReqDTO updatedOrder) {
//        return ordersRepository.findById(orderId)
//                .flatMap(existingOrder -> {
//                    existingOrder.setBilling_address(updatedOrder.getBilling_address());
//                    existingOrder.setCustomer_id(updatedOrder.getCustomer_id());
//                    existingOrder.setOrder_date(updatedOrder.getOrder_date());
//                    existingOrder.setOrder_status(updatedOrder.getOrder_status());
//                    existingOrder.setPayment_method(updatedOrder.getPayment_method());
//                    existingOrder.setShipping_address(updatedOrder.getShipping_address());
////                    existingOrder.setTotal_amount(updatedOrder.getTotal_amount().floatValue());
//
//                    return ordersRepository.save(existingOrder);
//                });
//    }
//    public Mono<Orders> createOrder(OrderReqDTO orderReqDTO) {
//        Orders order = Orders.builder()
//                .billing_address(orderReqDTO.getBilling_address())
//                .customer_id(orderReqDTO.getCustomer_id())
//                .order_date(orderReqDTO.getOrder_date())
//                .order_status(orderReqDTO.getOrder_status())
//                .payment_method(orderReqDTO.getPayment_method())
//                .shipping_address(orderReqDTO.getShipping_address())
//                .total_amount(orderReqDTO.getTotal_amount().floatValue())
//                .build();
//
//        List<OrderItem> orderItems = orderReqDTO.getOrderItems().stream()
//                .map(item -> OrderItem.builder()
//                        .price(item.getPrice())
//                        .product_id(item.getProduct_id())
//                        .quantity(item.getQuantity())
//                        .build())
//                .collect(Collectors.toList());
//
//        return ordersRepository.save(order)
//                .flatMap(savedOrder -> {
//                    orderItems.forEach(item -> item.setOrderId(savedOrder.getId()));
//                    return orderItemRepository.saveAll(orderItems).then(Mono.just(savedOrder));
//                });
//    }

//        Orders order = Orders.builder()
//                .billing_address(orderReqDTO.getBilling_address())
//                .customer_id(orderReqDTO.getCustomer_id())
//                .order_date(orderReqDTO.getOrder_date())
//                .order_status(orderReqDTO.getOrder_status())
//                .payment_method(orderReqDTO.getPayment_method())
//                .shipping_address(orderReqDTO.getShipping_address())
//                .total_amount(orderReqDTO.getTotal_amount().floatValue())  // Converting Double to float
//                .build();
//
//        return ordersRepository.save(order)
//                .flatMap(savedOrder -> {
//                    List<OrderItem> orderItems = orderReqDTO.getOrderItems();
//                    if (orderItems != null) {
//                        orderItems.forEach(item -> item.setOrder_id(savedOrder.getId()));
//                        return orderItemRepository.saveAll(orderItems).collectList()
//                                .map(savedItems -> savedOrder);
//                    } else {
//                        return Mono.just(savedOrder);
//                    }
//                });
//    }

//        OrderStatus orderStatus = OrderStatus.CREATED;
//
//        Orders order = Orders.builder()
//                .billing_address(orderReqDTO.getBilling_address())
//                .customer_id(orderReqDTO.getCustomer_id())
//                .order_date(orderReqDTO.getOrder_date())
////                .orderStatus(orderStatus) // Set the order status here
//                .payment_method(orderReqDTO.getPayment_method())
//                .shipping_address(orderReqDTO.getShipping_address())
//                .build();
//
//        List<OrderItem> orderItems = orderReqDTO.getOrderItems().stream()
//                .map(item -> OrderItem.builder()
//                        .price(item.getPrice())
//                        .product_id(item.getProduct_id())
//                        .quantity(item.getQuantity())
//                        .build())
//                .collect(Collectors.toList());
//
//        float totalAmount = calculateTotalAmount(orderItems);
//        order.setTotal_amount(totalAmount);
//
//        return ordersRepository.save(order)
//                .flatMap(savedOrder -> {
//                    orderItems.forEach(item -> item.setOrderId(savedOrder.getId()));
//                    return orderItemRepository.saveAll(orderItems)
//                            .then(Mono.just(OrderResponseDTO.builder()
//                                            .order_id(savedOrder.getId())
//                                            .customer_id(savedOrder.getCustomer_id())
//                                            .order_date(savedOrder.getOrder_date())
//                                            .payment_method(savedOrder.getPayment_method())
//                                            .billing_address(savedOrder.getBilling_address())
//                                            .shipping_address(savedOrder.getShipping_address())
//                                            .orderStatus(orderStatus)
//                                            .orderItems(orderItems.stream()
//                                                    .map(item -> OrderItemReqDTO.builder()
//                                                            .price(item.getPrice())
//                                                            .product_id(item.getProduct_id())
//                                                            .quantity(item.getQuantity())
//                                                            .build())
//                                                    .collect(Collectors.toList()))
//                                            .build()));
//                });
//                    return orderItemRepository.saveAll(orderItems).then(Mono.just(savedOrder));
//                });
//        OrderStatus orderStatus = OrderStatus.CREATED;
//        Orders order = Orders.builder()
//                .billing_address(orderReqDTO.getBilling_address())
//                .customer_id(orderReqDTO.getCustomer_id())
//                .order_date(orderReqDTO.getOrder_date())
////                .order_status(orderReqDTO.getOrder_status())
//                .payment_method(orderReqDTO.getPayment_method())
//                .shipping_address(orderReqDTO.getShipping_address())
//                .build();
//
//        List<OrderItem> orderItems = orderReqDTO.getOrderItems().stream()
//                .map(item -> OrderItem.builder()
//                        .price(item.getPrice())
//                        .product_id(item.getProduct_id())
//                        .quantity(item.getQuantity())
//                        .build())
//                .collect(Collectors.toList());
//
//        float totalAmount = calculateTotalAmount(orderItems);
//        order.setTotal_amount(totalAmount);
//
//        return ordersRepository.save(order)
//                .flatMap(savedOrder -> {
//                    orderItems.forEach(item -> item.setOrderId(savedOrder.getId()));
//                    return orderItemRepository.saveAll(orderItems).then(Mono.just(savedOrder));
//                });









//
//    public Mono<Orders> createOrder(OrderRequest orderRequest) {
//        Orders order = Orders.builder()
//                .billing_address(orderRequest.getBillingAddress())
//                .order_date(LocalDate.now())
//                .order_status("PENDING")
//                .payment_method(orderRequest.getPaymentMethod())
//                .shipping_address(orderRequest.getShippingAddress())
//                .total_amount(calculateTotalAmount(orderRequest.getItems()))
//                .build();
//
//        return ordersRepository.save(order);
//                OrderItem orderItem = OrderItem.builder()
//                        .
//                .flatMap(savedOrder -> Flux.fromIterable(orderRequest.getItems())
//                        .map(item -> OrderItem.builder()
//                                .price(item.getPrice())
//                                .product_id(item.getProductId())
//                                .quantity(item.getQuantity())
//                                .order_id(savedOrder.getId())
//                                .build())
//                        .flatMap(orderItemRepository::save)
//                        .then(Mono.just(savedOrder)));
//    }
//
//
//    public Flux<Orders> getAllOrders() {
//        return ordersRepository.findAll();
//    }
//
//    public Mono<Orders> updateOrder(Long orderId, OrderUpdateRequest updateRequest) {
//        return ordersRepository.findById(orderId)
//                .flatMap(existingOrder -> {
//                    existingOrder.setBilling_address(updateRequest.getBillingAddress());
//                    existingOrder.setOrder_status(updateRequest.getOrderStatus());
//                    existingOrder.setPayment_method(updateRequest.getPaymentMethod());
//                    existingOrder.setShipping_address(updateRequest.getShippingAddress());
//                    return ordersRepository.save(existingOrder);
//                });
//    }
//
//    private float calculateTotalAmount(List<OrderItemRequest> items) {
//        return (float) items.stream()
//                .mapToDouble(item -> item.getPrice() * item.getQuantity())
//                .sum();
//    }
//
//}

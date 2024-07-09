package com.ags.order.service.controller;

import com.ags.order.service.dto1.OrderItemUpdateDTO;
import com.ags.order.service.dto1.OrderReqDTO;
import com.ags.order.service.dto1.OrderResponseDTO;
import com.ags.order.service.dto1.OrderUpdateDTO;
import com.ags.order.service.model.OrderItem;
import com.ags.order.service.model.Orders;
import com.ags.order.service.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
@RestController
@RequestMapping("/api/order")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public Mono<OrderResponseDTO> createOrder(@RequestBody OrderReqDTO orderReqDTO) {
        return orderService.createOrder(orderReqDTO);
    }

    @GetMapping
    public Flux<Orders> getAllOrders() {
        return orderService.getAllOrders();
    }

    @PutMapping("/{orderId}")
    public Mono<Orders> updateOrderDetails(@PathVariable Long orderId, @RequestBody OrderUpdateDTO updatedOrder) {
        return orderService.updateOrderDetails(orderId, updatedOrder);
    }

    @PutMapping("/{orderId}/item/{itemId}")
    public Mono<OrderItem> updateOrderItem(@PathVariable Long orderId, @PathVariable Long itemId, @RequestBody OrderItemUpdateDTO updatedItem) {
    return orderService.updateOrderItem(orderId, itemId, updatedItem);
    }

    @DeleteMapping("/{orderId}/items/{itemId}")
    public Mono<Void> deleteOrderItem(@PathVariable Long orderId, @PathVariable Long itemId) {
        return orderService.deleteOrderItem(orderId, itemId);
    }



//    @PutMapping("/{itemId}")
//    public Mono<OrderItem> updateOrderItem(@PathVariable Long itemId, @RequestBody OrderItemUpdateDTO updatedItem) {
//        return orderService.updateOrderItem(itemId, updatedItem);
//    }
//    @PostMapping("/create")
//    public Mono<Orders> createOrder(@RequestBody OrderReqDTO orderReqDTO) {
//        return orderService.createOrder(orderReqDTO);
//    }
//
//    @PutMapping("/{orderId}")
//    public Mono<Orders> updateOrderByOrderId(@PathVariable Long orderId, @RequestBody OrderReqDTO updatedOrder) {
//        return orderService.updateOrderByOrderId(orderId, updatedOrder);
//    }
//
//    @GetMapping
//    public Flux<Orders> getAllOrders() {
//        return orderService.getAllOrders();
//    }

}
//    @PostMapping("/create")
//    public Mono<ResponseEntity<Orders>> createOrder(@RequestBody OrderRequest orderRequest) {
//        return orderService.createOrder(orderRequest)
//                .map(ResponseEntity::ok)
//                .defaultIfEmpty(ResponseEntity.badRequest().build());
//    }
//
//    @GetMapping("/all")
//    public Flux<Orders> getAllOrders() {
//        return orderService.getAllOrders();
//    }
//
//    @PutMapping("/update/{orderId}")
//    public Mono<ResponseEntity<Orders>> updateOrder(@PathVariable Long orderId, @RequestBody OrderUpdateRequest updateRequest) {
//        return orderService.updateOrder(orderId, updateRequest)
//                .map(ResponseEntity::ok)
//                .defaultIfEmpty(ResponseEntity.notFound().build());
//    }
//}

//@RestController
//@RequestMapping("/api/order")
//@Slf4j
//public class OrderController {
//    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

//    @Autowired
//    OrderItemService orderItemService;
//
//    @PostMapping("/create")
//    public Mono<ResponseEntity<WebResponse>> createOrderItem(@Valid @RequestBody OrderItemDTO request) {
//        logger.info("Received order item request: {}", request);
//        return orderItemService.createOrderItem(request)
//                .map(orderItem -> {
//                    String successMessage = "Order item created successfully";
//                    return ResponseEntity.ok(new WebResponse(successMessage));
//                })
//                .onErrorResume(ValidationException.class, e ->
//                        Mono.just(ResponseEntity.badRequest()
//                                .body(new WebResponse("Validation failed: " + e.getMessage())))
//                )
//                .onErrorResume(Exception.class, e -> {
//                    // Log the error for internal tracking
//                    System.err.println("Unexpected error occurred: " + e.getMessage());
//                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                            .body(new WebResponse("An unexpected error occurred")));
//                });
//    }
//
//    @GetMapping("/items")
//    public Flux<OrderItem> getAllOrderItems() {
//        return orderItemService.getAllOrderItems();
//    }
//
//    @GetMapping("/items/{id}")
//    public Mono<ResponseEntity<OrderItem>> getOrderItemById(@PathVariable Long id) {
//        return orderItemService.getOrderItemById(id)
//                .map(ResponseEntity::ok)
//                .onErrorResume(RuntimeException.class, e ->
//                        Mono.just(ResponseEntity.notFound().build()));
//    }
//
//
//
//    @PutMapping("/items/{id}")
//    public Mono<ResponseEntity<WebResponse>> updateOrderItem(@PathVariable Long id, @Valid @RequestBody OrderItemDTO request) {
//        return orderItemService.updateOrderItemById(id, request)
//                .map(updatedItem -> ResponseEntity.ok(new WebResponse("Order item updated successfully")))
//                .onErrorResume(RuntimeException.class, e ->
//                        Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
//                                .body(new WebResponse(e.getMessage()))));
//    }
//
//    @DeleteMapping("/items/{id}")
//    public Mono<ResponseEntity<WebResponse>> deleteOrderItem(@PathVariable Long id) {
//        return orderItemService.deleteOrderItem(id)
//                .thenReturn(ResponseEntity.ok(new WebResponse("Order item deleted successfully")))
//                .onErrorResume(Exception.class, e ->
//                        Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                                .body(new WebResponse("Failed to delete order item"))));
//    }
//
//    @DeleteMapping("/items")
//    public Mono<ResponseEntity<WebResponse>> deleteAllOrderItems() {
//        return orderItemService.deleteAllOrderItems()
//                .thenReturn(ResponseEntity.ok(new WebResponse("All order items deleted successfully")))
//                .onErrorResume(Exception.class, e ->
//                        Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                                .body(new WebResponse("Failed to delete all order items"))));
//    }
//}

















//nb:
//Order Service:
//        /order/create
///order/all
///order/update
//Product Service / Inventory Service
///inventory/deduct
///inventory/add
//Payment Service:
//        /payment/debit
///payment/credit
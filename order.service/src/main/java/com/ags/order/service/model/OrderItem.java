package com.ags.order.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("orderItem")
public class OrderItem {
    @Id
    private Long id;
    private float price;
    @Column("product_id")
    private Long product_id;
    private Integer quantity;
    @Column("order_id")
    private Long orderId;
}



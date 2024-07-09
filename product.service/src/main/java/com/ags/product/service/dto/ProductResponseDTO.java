package com.ags.product.service.dto;

import com.ags.product.service.status.InventoryStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDTO {
    private Long id;
    private String name;
    private float price;
    private String category;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime created_at;
    private String description;
    private String img_url;
    private Integer stock_quantity;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime updated_at;
    private InventoryStatus status;
}

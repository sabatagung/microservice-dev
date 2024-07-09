package com.ags.orchestrator.service.io;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ProductResponseDTO {
    private Long id;
    private float price;
    private Integer stock_quantity;
}
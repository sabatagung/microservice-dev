package com.ags.product.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdatedDTO {
    private String name;
    private float price;
    private String category;
    private String description;
    private String img_url;
}
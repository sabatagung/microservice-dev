package com.ags.product.service.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("product")
public class Product {
    @Id
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
}

/*
dari model ini buat dto untuk
ProductReqDTO
ProductUpdatedDTO
 */

/*
buatkan juga service dan controllernya
post mapping product/create
get mapping product/all
get mapping product/{id}
put mapping product/add/{id}? request param
put mapping product/deduct/{id}? request param

deduct dan add ini adalah endpoint untuk kita mengupdate stock di product deduct untuk mengurangi, add untuk menambah
berikan status .status(InventoryStatus.INSTOCK).build())); bila cukup,
berikan status .status(InventoryStatus.OUTOFSTOCK).build()));
 */
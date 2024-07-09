//package com.ags.product.service.utils;
//
//import com.ags.product.service.dto.ProductDTO;
//import com.ags.product.service.dto.ProductRequestDTO;
//import com.ags.product.service.model.Product;
//
//public class ProductMapper {
//    public static ProductDTO toDTO(Product product) {
//        return ProductDTO.builder()
//                .id(product.getId())
//                .name(product.getName())
//                .price(product.getPrice())
//                .category(product.getCategory())
//                .createdAt(product.getCreated_at())
//                .description(product.getDescription())
//                .imgUrl(product.getImg_url())
//                .stockQuantity(product.getStock_quantity())
//                .updatedAt(product.getUpdated_at())
//                .build();
//    }
//
//    public static Product toEntity(ProductRequestDTO dto) {
//        return Product.builder()
//                .name(dto.getName())
//                .price(dto.getPrice())
//                .category(dto.getCategory())
//                .description(dto.getDescription())
//                .img_url(dto.getImgUrl())
//                .stock_quantity(dto.getStockQuantity())
//                .build();
//    }
//
//    public static void updateEntityFromDTO(Product product, ProductRequestDTO dto) {
//        product.setName(dto.getName());
//        product.setPrice(dto.getPrice());
//        product.setCategory(dto.getCategory());
//        product.setDescription(dto.getDescription());
//        product.setImg_url(dto.getImgUrl());
//        product.setStock_quantity(dto.getStockQuantity());
//    }
//}

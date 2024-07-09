package com.ags.product.service.repository;

import com.ags.product.service.model.Product;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ProductRepository extends R2dbcRepository <Product, Long> {
    Flux<Product> findByCategory(String category);
}

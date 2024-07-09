package com.ags.product.service.controller;

import com.ags.product.service.dto.ProductReqDTO;
import com.ags.product.service.dto.ProductResponseDTO;
import com.ags.product.service.exception.InsufficientStockException;
import com.ags.product.service.service.ProductService;
import com.ags.product.service.status.InventoryStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/create")
    public Mono<ResponseEntity<ProductResponseDTO>> createProduct(@RequestBody ProductReqDTO productReqDTO) {
        return productService.createProduct(productReqDTO)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/all")
    public Flux<ProductResponseDTO> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<ProductResponseDTO>> getProductById(@PathVariable Long id) {
        return productService.getProductById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/add/{id}")
    public Mono<ResponseEntity<ProductResponseDTO>> addStock(@PathVariable Long id, @RequestParam Integer quantity) {
        return productService.addStock(id, quantity)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping("/deduct/{id}")
    public Mono<ResponseEntity<ProductResponseDTO>> deductStock(@PathVariable Long id, @RequestParam Integer quantity) {
        return productService.deductStock(id, quantity)
                .map(ResponseEntity::ok)
                .onErrorResume(InsufficientStockException.class, e ->
                        Mono.just(ResponseEntity.badRequest().body(ProductResponseDTO.builder()
                                .status(InventoryStatus.OUTOFSTOCK)
                                .build())))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}

//import com.ags.product.service.dto.ProductDTO;
//import com.ags.product.service.dto.ProductRequestDTO;
//import com.ags.product.service.model.Product;
//import com.ags.product.service.service.ProductService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.*;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//@RestController
//@RequestMapping("/api/products")
//public class ProductController {
//
//    @Autowired
//    private ProductService productService;
//
//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    public Mono<Product> createProduct(@RequestBody ProductRequestDTO request) {
//        return productService.createProduct(request);
//    }
//
//    @GetMapping
//    public Flux<ProductDTO> getAllProducts() {
//        return productService.getAllProducts();
//    }
//
//    @GetMapping("/{id}")
//    public Mono<ProductDTO> getProductById(@PathVariable Long id) {
//        return productService.getProductById(id);
//    }
//
////    @PutMapping("/{id}")
////    public Mono<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductRequestDTO request) {
////        return productService.updateProduct(id, request);
////    }
//
//    @DeleteMapping("/{id}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public Mono<Void> deleteProduct(@PathVariable Long id) {
//        return productService.deleteProduct(id);
//    }
//
//    @DeleteMapping
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public Mono<Void> deleteAllProducts() {
//        return productService.deleteAllProducts();
//    }
//
//    @GetMapping("/category/{category}")
//    public Flux<ProductDTO> getProductsByCategory(@PathVariable String category) {
//        return productService.getProductsByCategory(category);
//    }
//}

//import com.ags.product.service.dto.ProductDTO;
//import com.ags.product.service.dto.ProductRequestDTO;
//import com.ags.product.service.model.Product;
//import com.ags.product.service.service.ProductService;
//import jakarta.validation.Valid;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//
//@RestController
//@RequestMapping("/api/products")
//@Validated
//@Slf4j
//public class ProductController {
//
//    private final ProductService productService;
//
//    public ProductController(ProductService productService) {
//        this.productService = productService;
//    }
//
//    @PostMapping
//    public Mono<ResponseEntity<Product>> createProduct(@Valid @RequestBody ProductRequestDTO request) {
//        return productService.createProduct(request)
//                .map(product -> ResponseEntity.status(HttpStatus.CREATED).body(product));
//    }
//
//    @GetMapping
//    public Flux<Product> getAllProducts() {
//        return productService.getAllProducts();
//    }
//
//    @GetMapping("/{id}")
//    public Mono<ResponseEntity<Product>> getProductById(@PathVariable Long id) {
//        return productService.getProductById(id)
//                .map(ResponseEntity::ok)
//                .defaultIfEmpty(ResponseEntity.notFound().build());
//    }
//
//    @PutMapping("/{id}")
//    public Mono<ResponseEntity<Product>> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductRequestDTO request) {
//        return productService.updateProduct(id, request)
//                .map(ResponseEntity::ok)
//                .defaultIfEmpty(ResponseEntity.notFound().build());
//    }
//
//    @DeleteMapping("/{id}")
//    public Mono<ResponseEntity<Void>> deleteProduct(@PathVariable Long id) {
//        return productService.deleteProduct(id)
//                .then(Mono.just(ResponseEntity.noContent().<Void>build()));
//    }
//
//    @DeleteMapping
//    public Mono<ResponseEntity<Void>> deleteAllProducts() {
//        return productService.deleteAllProducts()
//                .then(Mono.just(ResponseEntity.noContent().<Void>build()));
//    }
//
//    @GetMapping("/category/{category}")
//    public Flux<ProductDTO> getProductsByCategory(@PathVariable String category) {
//        return productService.getProductsByCategory(category);
//    }
//
//}

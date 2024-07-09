package com.ags.product.service.service;

import com.ags.product.service.dto.ProductReqDTO;
import com.ags.product.service.dto.ProductResponseDTO;
import com.ags.product.service.exception.InsufficientStockException;
import com.ags.product.service.exception.ProductNotFoundException;
import com.ags.product.service.model.Product;
import com.ags.product.service.repository.ProductRepository;
import com.ags.product.service.status.InventoryStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Mono<ProductResponseDTO> createProduct(ProductReqDTO productReqDTO) {
        Product product = Product.builder()
                .name(productReqDTO.getName())
                .price(productReqDTO.getPrice())
                .category(productReqDTO.getCategory())
                .description(productReqDTO.getDescription())
                .img_url(productReqDTO.getImg_url())
                .stock_quantity(productReqDTO.getStock_quantity())
                .created_at(LocalDateTime.now())
                 .updated_at(LocalDateTime.now())
                .build();

        return productRepository.save(product)
                .map(this::mapToResponseDTO);
    }

    public Flux<ProductResponseDTO> getAllProducts() {
        return productRepository.findAll()
                .map(this::mapToResponseDTO);
    }

    public Mono<ProductResponseDTO> getProductById(Long id) {
        return productRepository.findById(id)
                .map(this::mapToResponseDTO)
                .switchIfEmpty(Mono.error(new ProductNotFoundException("Product not found with id: " + id)));
    }

    public Mono<ProductResponseDTO> addStock(Long id, Integer quantity) {
        return productRepository.findById(id)
                .flatMap(product -> {
                    product.setStock_quantity(product.getStock_quantity() + quantity);
                    product.setUpdated_at(LocalDateTime.now());
                    return productRepository.save(product);
                })
                .map(this::mapToResponseDTO)
                .switchIfEmpty(Mono.error(new ProductNotFoundException("Product not found with id: " + id)));
    }

    public Mono<ProductResponseDTO> deductStock(Long id, Integer quantity) {
        return productRepository.findById(id)
                .flatMap(product -> {
                    if (product.getStock_quantity() < quantity) {
                        return Mono.error(new InsufficientStockException("Insufficient stock for product with id: " + id));
                    }
                    product.setStock_quantity(product.getStock_quantity() - quantity);
                    product.setUpdated_at(LocalDateTime.now());
                    return productRepository.save(product);
                })
                .map(this::mapToResponseDTO)
                .switchIfEmpty(Mono.error(new ProductNotFoundException("Product not found with id: " + id)));
    }

    private ProductResponseDTO mapToResponseDTO(Product product) {
        InventoryStatus status = product.getStock_quantity() >= 0 ? InventoryStatus.INSTOCK : InventoryStatus.OUTOFSTOCK;
        return ProductResponseDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .category(product.getCategory())
                .created_at(product.getCreated_at())
                .description(product.getDescription())
                .img_url(product.getImg_url())
                .stock_quantity(product.getStock_quantity())
                .updated_at(product.getUpdated_at())
                .status(status)
                .build();
    }
}

//import com.ags.product.service.dto.ProductDTO;
//import com.ags.product.service.utils.ProductMapper;
//import com.ags.product.service.dto.ProductRequestDTO;
//import com.ags.product.service.model.Product;
//import com.ags.product.service.repository.ProductRepository;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//import org.springframework.stereotype.Service;
//import org.springframework.beans.factory.annotation.Autowired;
//import java.time.LocalDateTime;
//
//@Service
//public class ProductService {
//
//    @Autowired
//    private ProductRepository productRepository;
//
////    @Autowired
////    private KafkaProducerService kafkaProducerService;
//
////    public Mono<ProductDTO> createProduct(ProductRequestDTO request) {
////        Product product = ProductMapper.toEntity(request);
////        product.setCreated_at(LocalDateTime.now());
////        product.setUpdated_at(LocalDateTime.now());
////        return productRepository.save(product)
////                .map(ProductMapper::toDTO);
////    }
//
//    public Mono<Product> createProduct(ProductRequestDTO request) {
//        // Membuat objek Product dari ProductRequestDTO
//        Product product = Product.builder()
//                .name(request.getName())
//                .price(request.getPrice())
//                .category(request.getCategory())
//                .description(request.getDescription())
//                .img_url(request.getImgUrl())
//                .stock_quantity(request.getStockQuantity())
//                .created_at(LocalDateTime.now())
//                .updated_at(LocalDateTime.now())
//                .build();
//
//        // Simpan ke dalam database menggunakan repository
//        return productRepository.save(product)
//                .onErrorResume(e -> {
//                    // Mengatasi kesalahan yang terjadi saat penyimpanan
//                    // Di sini kita bisa melakukan rollback atau menangani kesalahan lainnya
//                    System.err.println("Failed to save product: " + e.getMessage());
//                    // Mengembalikan Mono.empty() atau nilai default lainnya
//                    return Mono.empty();
//                });
//
//    }
//
//    public Flux<ProductDTO> getAllProducts() {
//        return productRepository.findAll()
//                .map(ProductMapper::toDTO);
//    }
//
//    public Mono<ProductDTO> getProductById(Long id) {
//        return productRepository.findById(id)
//                .map(ProductMapper::toDTO);
//    }
//
//    public Mono<Void> deleteProduct(Long id) {
//        return productRepository.deleteById(id);
//    }
//
//    public Mono<Void> deleteAllProducts() {
//        return productRepository.deleteAll();
//    }
//
//    public Flux<ProductDTO> getProductsByCategory(String category) {
//        return productRepository.findByCategory(category)
//                .map(ProductMapper::toDTO);
//    }
//}



//    public Mono<ProductDTO> updateProduct(Long id, ProductRequestDTO request) {
//        return productRepository.findById(id)
//                .flatMap(existingProduct -> {
//                    ProductMapper.updateEntityFromDTO(existingProduct, request);
//                    existingProduct.setUpdated_at(LocalDateTime.now());
//                    return productRepository.save(existingProduct);
//                })
//                .map(ProductMapper::toDTO);
//    }
//public Mono<ProductDTO> updateProduct(Long id, ProductRequestDTO request) {
//    return productRepository.findById(id)
//            .flatMap(existingProduct -> {
//                ProductMapper.updateEntityFromDTO(existingProduct, request);
//                existingProduct.setUpdated_at(LocalDateTime.now());
//                return productRepository.save(existingProduct);
//            })
//            .doOnSuccess(updatedProduct -> kafkaProducerService.sendProductEvent("PRODUCT_UPDATED", updatedProduct))
//            .map(ProductMapper::toDTO);
//}
//import com.ags.product.service.dto.ProductRequestDTO;
//import com.ags.product.service.exception.InsufficientStockException;
//import com.ags.product.service.exception.ProductNotFoundException;
//import com.ags.product.service.kafka.KafkaProducer;
//import com.ags.product.service.model.Product;
//import com.ags.product.service.repository.ProductRepository;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import java.time.LocalDateTime;
//
//@Service
//@Transactional
//@Slf4j
//public class ProductService {
//    private final ProductRepository productRepository;
//    private final KafkaProducer kafkaProducer;
//    private final Logger logger = LoggerFactory.getLogger(ProductService.class);
//
//    public ProductService(ProductRepository productRepository, KafkaProducer kafkaProducer) {
//        this.productRepository = productRepository;
//        this.kafkaProducer = kafkaProducer;
//    }
//
//    public Mono<Product> createProduct(ProductRequestDTO request) {
//        return Mono.just(request)
//                .map(this::mapToProduct)
//                .flatMap(productRepository::save)
//                .doOnSuccess(product -> {
//                    logger.info("Created product with ID: {}", product.getId());
//                    kafkaProducer.sendProductRequest("product-topic", request);
//                })
//                .doOnError(e -> logger.error("Error creating product", e));
//    }
//
//
//
//    public Flux<Product> getAllProducts() {
//        return productRepository.findAll()
//                .doOnComplete(() -> logger.info("Retrieved all products"))
//                .doOnError(e -> logger.error("Error retrieving products", e));
//    }
//
//    public Mono<Product> getProductById(Long id) {
//        return productRepository.findById(id)
//                .switchIfEmpty(Mono.error(new ProductNotFoundException(String.valueOf(id))))
//                .doOnSuccess(product -> logger.info("Retrieved product with ID: {}", id))
//                .doOnError(e -> logger.error("Error retrieving product with ID: {}", id, e));
//    }
//
//    public Mono<Product> updateProduct(Long id, ProductRequestDTO request) {
//        return productRepository.findById(id)
//                .switchIfEmpty(Mono.error(new ProductNotFoundException(String.valueOf(id))))
//                .map(existingProduct -> updateProductFields(existingProduct, request))
//                .flatMap(productRepository::save)
//                .doOnSuccess(product -> logger.info("Updated product with ID: {}", id))
//                .doOnError(e -> logger.error("Error updating product with ID: {}", id, e));
//    }
//
//    public Mono<Void> deleteProduct(Long id) {
//        return productRepository.findById(id)
//                .switchIfEmpty(Mono.error(new ProductNotFoundException(String.valueOf(id))))
//                .flatMap(productRepository::delete)
//                .doOnSuccess(v -> logger.info("Deleted product with ID: {}", id))
//                .doOnError(e -> logger.error("Error deleting product with ID: {}", id, e));
//    }
//
//    public Mono<Void> deleteAllProducts() {
//        return productRepository.deleteAll()
//                .doOnSuccess(v -> logger.info("Deleted all products"))
//                .doOnError(e -> logger.error("Error deleting all products", e));
//    }
//
//    public Flux<Product> getProductsByCategory(String category) {
//        return productRepository.findByCategory(category)
//                .doOnComplete(() -> logger.info("Retrieved products for category: {}", category))
//                .doOnError(e -> logger.error("Error retrieving products for category: {}", category, e));
//    }
//
//    private  Product mapToProduct(ProductRequestDTO request) {
//        return Product.builder()
//                .name(request.getName())
//                .price(request.getPrice())
//                .category(request.getCategory())
//                .description(request.getDescription())
//                .img_url(request.getImg_url())
//                .stock_quantity(request.getStock_quantity())
//                .created_at(LocalDateTime.now())
//                .updated_at(LocalDateTime.now())
//                .build();
//    }
//
//    private Product updateProductFields(Product product, ProductRequestDTO request) {
//        product.setName(request.getName());
//        product.setPrice(request.getPrice());
//        product.setCategory(request.getCategory());
//        product.setDescription(request.getDescription());
//        product.setImg_url(request.getImg_url());
//        product.setStock_quantity(request.getStock_quantity());
//        product.setUpdated_at(LocalDateTime.now());
//        return product;
//    }
//    public Mono<Void> reduceStock(Long productId, int quantity) {
//        return productRepository.findById(productId)
//                .switchIfEmpty(Mono.error(new ProductNotFoundException(String.valueOf(productId))))
//                .flatMap(product -> {
//                    int newQuantity = product.getStock_quantity() - quantity;
//                    if (newQuantity < 0) {
//                        return Mono.error(new InsufficientStockException(productId));
//                    }
//                    product.setStock_quantity(newQuantity);
//                    return productRepository.save(product);
//                })
//                .doOnSuccess(product -> log.info("Reduced stock for product ID: {}", productId))
//                .doOnError(e -> log.error("Error reducing stock for product ID: {}", productId, e))
//                .then();
//    }
//}


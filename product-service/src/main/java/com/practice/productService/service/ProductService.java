package com.practice.productService.service;

import com.practice.productService.dto.ProductRequest;
import com.practice.productService.dto.ProductResponse;
import com.practice.productService.entity.Product;
import com.practice.productService.exception.ProductNotFoundException;
import com.practice.productService.mapper.ProductMapper;
import com.practice.productService.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository,
                          ProductMapper productMapper) {
        this.productRepository = productRepository;
            this.productMapper = productMapper;
    }

    public ProductResponse createProduct(ProductRequest request) {
        Product product = productMapper.toEntity(request);
       Product savedProduct = productRepository.save(product);
       return productMapper.toResponse(savedProduct);
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }

    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("product not found with id : " +id));
        return productMapper.toResponse(product);
    }

    public ProductResponse updateProductById(Long id, ProductRequest request) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("product not found with id : " +id));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());

        Product updatedProduct = productRepository.save(product);

        return productMapper.toResponse(updatedProduct);
    }

    public void deleteProduct(Long id) {

        Product product = productRepository.findById(id)
                        .orElseThrow(() -> new ProductNotFoundException("Product not found with id : " +id));
        productRepository.delete(product);
    }
}

package com.foody.delivery.product.service;

import com.foody.delivery.common.exception.ResourceNotFoundException;
import com.foody.delivery.product.dto.ProductRequest;
import com.foody.delivery.product.dto.ProductResponse;
import com.foody.delivery.product.model.Product;
import com.foody.delivery.product.model.ProductCategory;
import com.foody.delivery.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> findAll(Boolean availableOnly) {
        List<Product> products = (availableOnly != null && availableOnly)
                ? productRepository.findByAvailableTrue()
                : productRepository.findAll();

        return products.stream().map(ProductResponse::fromEntity).toList();
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> findByCategory(ProductCategory category) {
        return productRepository.findByCategory(category).stream()
                .map(ProductResponse::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductResponse findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com ID: " + id));
        return ProductResponse.fromEntity(product);
    }

    @Transactional
    public ProductResponse create(ProductRequest request) {
        Product product = new Product(
                request.name(),
                request.description(),
                request.price(),
                request.category(),
                request.imageUrl()
        );
        if (request.available() != null) {
            product.setAvailable(request.available());
        }

        Product saved = productRepository.save(product);
        return ProductResponse.fromEntity(saved);
    }

    @Transactional
    public ProductResponse update(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com ID: " + id));

        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setCategory(request.category());
        if (request.imageUrl() != null) {
            product.setImageUrl(request.imageUrl());
        }
        if (request.available() != null) {
            product.setAvailable(request.available());
        }

        Product updated = productRepository.save(product);
        return ProductResponse.fromEntity(updated);
    }

    @Transactional
    public void delete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com ID: " + id));
        productRepository.delete(product);
    }
}

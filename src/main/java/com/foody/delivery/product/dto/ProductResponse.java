package com.foody.delivery.product.dto;

import com.foody.delivery.product.model.Product;
import com.foody.delivery.product.model.ProductCategory;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductResponse(
    Long id,
    String name,
    String description,
    BigDecimal price,
    ProductCategory category,
    String categoryDisplayName,
    String imageUrl,
    Boolean available,
    LocalDateTime createdAt
) {
    public static ProductResponse fromEntity(Product product) {
        return new ProductResponse(
            product.getId(),
            product.getName(),
            product.getDescription(),
            product.getPrice(),
            product.getCategory(),
            product.getCategory() != null ? product.getCategory().getDisplayName() : null,
            product.getImageUrl(),
            product.getAvailable(),
            product.getCreatedAt()
        );
    }
}

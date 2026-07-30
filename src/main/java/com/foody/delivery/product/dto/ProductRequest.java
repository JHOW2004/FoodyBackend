package com.foody.delivery.product.dto;

import com.foody.delivery.product.model.ProductCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProductRequest(
    @NotBlank(message = "Nome do produto é obrigatório")
    String name,

    String description,

    @NotNull(message = "Preço é obrigatório")
    @Positive(message = "Preço deve ser positivo")
    BigDecimal price,

    @NotNull(message = "Categoria é obrigatória")
    ProductCategory category,

    String imageUrl,

    Boolean available
) {}

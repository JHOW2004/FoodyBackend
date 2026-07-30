package com.foody.delivery.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Item individual de um pedido")
public class OrderItemRequest {

    @Schema(description = "Nome do produto", example = "Hambúrguer Artesanal Foody")
    @NotBlank(message = "O nome do produto é obrigatório")
    private String productName;

    @Schema(description = "Quantidade solicitada do produto", example = "2")
    @NotNull(message = "A quantidade é obrigatória")
    @Min(value = 1, message = "A quantidade mínima é 1")
    private Integer quantity;

    @Schema(description = "Preço unitário do produto em Reais (R$)", example = "35.50")
    @NotNull(message = "O preço unitário é obrigatório")
    @DecimalMin(value = "0.01", message = "O preço unitário deve ser maior que zero")
    private BigDecimal unitPrice;
}

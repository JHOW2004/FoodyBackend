package com.foody.delivery.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dados para criação de um novo pedido de delivery")
public class CreateOrderRequest {

    @Schema(description = "Nome do cliente solicitante do pedido", example = "João da Silva")
    @NotBlank(message = "O nome do cliente é obrigatório")
    private String customerName;

    @Schema(description = "Endereço completo para entrega do pedido", example = "Av. Paulista, 1000 - Bela Vista, São Paulo - SP")
    @NotBlank(message = "O endereço de entrega é obrigatório")
    private String deliveryAddress;

    @Schema(description = "Lista dos produtos/itens incluídos no pedido")
    @NotEmpty(message = "O pedido deve conter pelo menos 1 item")
    @Valid
    private List<OrderItemRequest> items;
}

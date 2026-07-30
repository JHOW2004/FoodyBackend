package com.foody.delivery.order.dto;

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
public class UpdateOrderRequest {

    @NotBlank(message = "O nome do cliente é obrigatório")
    private String customerName;

    @NotBlank(message = "O endereço de entrega é obrigatório")
    private String deliveryAddress;

    @NotEmpty(message = "O pedido deve conter pelo menos 1 item")
    @Valid
    private List<OrderItemRequest> items;
}

package com.foody.delivery.order.dto;

import com.foody.delivery.domain.model.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Solicitação de atualização do status de um pedido")
public class UpdateOrderStatusRequest {

    @Schema(description = "Novo status do pedido (RECEBIDO, EM_PREPARO, SAIU_PARA_ENTREGA, ENTREGUE, CANCELADO)", example = "EM_PREPARO")
    @NotNull(message = "O status é obrigatório")
    private OrderStatus status;
}

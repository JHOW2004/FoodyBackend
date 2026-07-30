package com.foody.delivery.order.dto;

import com.foody.delivery.domain.model.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Resposta contendo os detalhes completos do pedido")
public class OrderResponse {

    @Schema(description = "ID único do pedido", example = "1")
    private Long id;

    @Schema(description = "Nome do cliente", example = "João da Silva")
    private String customerName;

    @Schema(description = "Endereço de entrega", example = "Av. Paulista, 1000 - São Paulo/SP")
    private String deliveryAddress;

    @Schema(description = "Status atual do pedido", example = "RECEBIDO")
    private OrderStatus status;

    @Schema(description = "Valor total do pedido (soma dos subtotais dos itens)", example = "83.00")
    private BigDecimal totalPrice;

    @Schema(description = "Lista dos itens do pedido")
    private List<OrderItemResponse> items;

    @Schema(description = "Histórico de auditoria e alterações de status do pedido")
    private List<OrderStatusHistoryResponse> history;

    @Schema(description = "Data e hora de criação do pedido")
    private LocalDateTime createdAt;

    @Schema(description = "Data e hora da última atualização do pedido")
    private LocalDateTime updatedAt;
}

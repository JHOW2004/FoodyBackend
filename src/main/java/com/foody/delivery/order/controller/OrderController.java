package com.foody.delivery.order.controller;

import com.foody.delivery.order.dto.*;
import com.foody.delivery.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Pedidos", description = "Endpoints para criação, consulta, atualização de status e histórico de pedidos")
@SecurityRequirement(name = "bearerAuth")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "Criar novo pedido", description = "Cadastra um novo pedido de delivery com itens e endereço de entrega.")
    @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Erro de validação nos dados do pedido")
    @ApiResponse(responseCode = "401", description = "Não autenticado")
    public ResponseEntity<OrderResponse> createOrder(
            @Valid @RequestBody CreateOrderRequest request,
            Principal principal
    ) {
        String username = principal != null ? principal.getName() : "SISTEMA";
        OrderResponse response = orderService.createOrder(request, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Listar todos os pedidos", description = "Retorna todos os pedidos cadastrados ordenados do mais recente para o mais antigo.")
    @ApiResponse(responseCode = "200", description = "Lista de pedidos retornada com sucesso")
    @ApiResponse(responseCode = "401", description = "Não autenticado")
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pedido por ID", description = "Retorna os detalhes completos de um pedido específico.")
    @ApiResponse(responseCode = "200", description = "Pedido encontrado")
    @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    @ApiResponse(responseCode = "401", description = "Não autenticado")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        OrderResponse order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualizar status do pedido", description = "Atualiza o status de um pedido (RECEBIDO, EM_PREPARO, SAIU_PARA_ENTREGA, ENTREGUE, CANCELADO).")
    @ApiResponse(responseCode = "200", description = "Status do pedido atualizado")
    @ApiResponse(responseCode = "400", description = "Transição de status inválida")
    @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    @ApiResponse(responseCode = "401", description = "Não autenticado")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateOrderStatusRequest request,
            Principal principal
    ) {
        String username = principal != null ? principal.getName() : "SISTEMA";
        OrderResponse updatedOrder = orderService.updateOrderStatus(id, request.getStatus(), username);
        return ResponseEntity.ok(updatedOrder);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Editar pedido", description = "Atualiza os dados de cliente, endereço e itens do pedido.")
    @ApiResponse(responseCode = "200", description = "Pedido atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos ou pedido não pode ser editado")
    @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    public ResponseEntity<OrderResponse> updateOrder(
            @PathVariable Long id,
            @Valid @RequestBody UpdateOrderRequest request,
            Principal principal
    ) {
        String username = principal != null ? principal.getName() : "SISTEMA";
        OrderResponse updatedOrder = orderService.updateOrder(id, request, username);
        return ResponseEntity.ok(updatedOrder);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir pedido", description = "Remove um pedido do sistema.")
    @ApiResponse(responseCode = "204", description = "Pedido excluído com sucesso")
    @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/history")
    @Operation(summary = "Obter histórico do pedido", description = "Retorna a linha do tempo completa de auditoria de alterações do pedido.")
    @ApiResponse(responseCode = "200", description = "Histórico retornado com sucesso")
    @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    public ResponseEntity<List<OrderStatusHistoryResponse>> getOrderHistory(@PathVariable Long id) {
        List<OrderStatusHistoryResponse> history = orderService.getOrderHistory(id);
        return ResponseEntity.ok(history);
    }
}

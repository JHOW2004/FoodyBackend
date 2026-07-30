package com.foody.delivery.orders.service;

import com.foody.delivery.common.exception.InvalidOrderStatusException;
import com.foody.delivery.common.exception.ResourceNotFoundException;
import com.foody.delivery.domain.model.Order;
import com.foody.delivery.domain.model.OrderItem;
import com.foody.delivery.domain.model.OrderStatus;
import com.foody.delivery.domain.model.OrderStatusHistory;
import com.foody.delivery.domain.repository.OrderRepository;
import com.foody.delivery.orders.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request, String currentUserEmail) {
        Order order = Order.builder()
                .customerName(request.getCustomerName())
                .deliveryAddress(request.getDeliveryAddress())
                .status(OrderStatus.RECEBIDO)
                .build();

        if (request.getItems() != null) {
            for (OrderItemRequest itemReq : request.getItems()) {
                OrderItem item = OrderItem.builder()
                        .productName(itemReq.getProductName())
                        .quantity(itemReq.getQuantity())
                        .unitPrice(itemReq.getUnitPrice())
                        .build();
                item.calculateSubTotal();
                order.addItem(item);
            }
        }

        order.calculateTotalPrice();
        
        String updater = (currentUserEmail != null && !currentUserEmail.isBlank()) ? currentUserEmail : "SISTEMA";
        order.addHistoryEntry(null, OrderStatus.RECEBIDO, "Pedido criado com sucesso", updater);

        Order savedOrder = orderRepository.save(order);
        return mapToOrderResponse(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::mapToOrderResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long id) {
        Order order = findOrderById(id);
        return mapToOrderResponse(order);
    }

    @Transactional
    public OrderResponse updateOrderStatus(Long id, OrderStatus newStatus, String currentUserEmail) {
        Order order = findOrderById(id);
        OrderStatus oldStatus = order.getStatus();

        if (oldStatus == newStatus) {
            return mapToOrderResponse(order);
        }

        if (!oldStatus.canTransitionTo(newStatus)) {
            throw new InvalidOrderStatusException(
                    String.format("Não é possível alterar o status do pedido #%d de '%s' para '%s'.", id, oldStatus, newStatus)
            );
        }

        order.setStatus(newStatus);
        String updater = (currentUserEmail != null && !currentUserEmail.isBlank()) ? currentUserEmail : "SISTEMA";
        order.addHistoryEntry(
                oldStatus,
                newStatus,
                String.format("Status alterado de %s para %s", oldStatus, newStatus),
                updater
        );

        Order updatedOrder = orderRepository.save(order);
        return mapToOrderResponse(updatedOrder);
    }

    @Transactional
    public OrderResponse updateOrder(Long id, UpdateOrderRequest request, String currentUserEmail) {
        Order order = findOrderById(id);

        if (order.getStatus() == OrderStatus.ENTREGUE || order.getStatus() == OrderStatus.CANCELADO) {
            throw new InvalidOrderStatusException("Não é possível editar um pedido com status " + order.getStatus());
        }

        order.setCustomerName(request.getCustomerName());
        order.setDeliveryAddress(request.getDeliveryAddress());

        // Atualizar itens
        order.getItems().clear();
        if (request.getItems() != null) {
            for (OrderItemRequest itemReq : request.getItems()) {
                OrderItem item = OrderItem.builder()
                        .productName(itemReq.getProductName())
                        .quantity(itemReq.getQuantity())
                        .unitPrice(itemReq.getUnitPrice())
                        .build();
                item.calculateSubTotal();
                order.addItem(item);
            }
        }

        order.calculateTotalPrice();
        String updater = (currentUserEmail != null && !currentUserEmail.isBlank()) ? currentUserEmail : "SISTEMA";
        order.addHistoryEntry(order.getStatus(), order.getStatus(), "Dados e itens do pedido atualizados", updater);

        Order updatedOrder = orderRepository.save(order);
        return mapToOrderResponse(updatedOrder);
    }

    @Transactional
    public void deleteOrder(Long id) {
        Order order = findOrderById(id);
        orderRepository.delete(order);
    }

    @Transactional(readOnly = true)
    public List<OrderStatusHistoryResponse> getOrderHistory(Long id) {
        Order order = findOrderById(id);
        return order.getHistory().stream()
                .map(this::mapToHistoryResponse)
                .collect(Collectors.toList());
    }

    private Order findOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado com o ID: " + id));
    }

    private OrderResponse mapToOrderResponse(Order order) {
        List<OrderItemResponse> itemResponses = order.getItems() == null ? List.of() :
                order.getItems().stream()
                        .map(item -> OrderItemResponse.builder()
                                .id(item.getId())
                                .productName(item.getProductName())
                                .quantity(item.getQuantity())
                                .unitPrice(item.getUnitPrice())
                                .subTotal(item.getSubTotal() != null ? item.getSubTotal() : item.calculateSubTotal())
                                .build())
                        .collect(Collectors.toList());

        List<OrderStatusHistoryResponse> historyResponses = order.getHistory() == null ? List.of() :
                order.getHistory().stream()
                        .map(this::mapToHistoryResponse)
                        .collect(Collectors.toList());

        return OrderResponse.builder()
                .id(order.getId())
                .customerName(order.getCustomerName())
                .deliveryAddress(order.getDeliveryAddress())
                .status(order.getStatus())
                .totalPrice(order.getTotalPrice())
                .items(itemResponses)
                .history(historyResponses)
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }

    private OrderStatusHistoryResponse mapToHistoryResponse(OrderStatusHistory history) {
        return OrderStatusHistoryResponse.builder()
                .id(history.getId())
                .previousStatus(history.getPreviousStatus())
                .newStatus(history.getNewStatus())
                .description(history.getDescription())
                .updatedBy(history.getUpdatedBy())
                .createdAt(history.getCreatedAt())
                .build();
    }
}

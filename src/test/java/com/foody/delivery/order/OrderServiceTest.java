package com.foody.delivery.order;

import com.foody.delivery.common.exception.InvalidOrderStatusException;
import com.foody.delivery.common.exception.ResourceNotFoundException;
import com.foody.delivery.domain.model.Order;
import com.foody.delivery.domain.model.OrderItem;
import com.foody.delivery.domain.model.OrderStatus;
import com.foody.delivery.domain.repository.OrderRepository;
import com.foody.delivery.order.dto.CreateOrderRequest;
import com.foody.delivery.order.dto.OrderItemRequest;
import com.foody.delivery.order.dto.OrderResponse;
import com.foody.delivery.order.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    private CreateOrderRequest createOrderRequest;
    private Order mockOrder;

    @BeforeEach
    void setUp() {
        OrderItemRequest itemReq = OrderItemRequest.builder()
                .productName("Hambúrguer Artesanal")
                .quantity(2)
                .unitPrice(new BigDecimal("30.00"))
                .build();

        createOrderRequest = CreateOrderRequest.builder()
                .customerName("Maria Oliveira")
                .deliveryAddress("Rua das Flores, 123")
                .items(List.of(itemReq))
                .build();

        OrderItem item = OrderItem.builder()
                .id(1L)
                .productName("Hambúrguer Artesanal")
                .quantity(2)
                .unitPrice(new BigDecimal("30.00"))
                .subTotal(new BigDecimal("60.00"))
                .build();

        mockOrder = Order.builder()
                .id(100L)
                .customerName("Maria Oliveira")
                .deliveryAddress("Rua das Flores, 123")
                .status(OrderStatus.RECEBIDO)
                .totalPrice(new BigDecimal("60.00"))
                .items(new ArrayList<>(List.of(item)))
                .build();
    }

    @Test
    @DisplayName("Deve criar um pedido com sucesso e calcular o valor total")
    void shouldCreateOrderSuccessfully() {
        when(orderRepository.save(any(Order.class))).thenReturn(mockOrder);

        OrderResponse response = orderService.createOrder(createOrderRequest, "admin@foody.com");

        assertNotNull(response);
        assertEquals(100L, response.getId());
        assertEquals("Maria Oliveira", response.getCustomerName());
        assertEquals(OrderStatus.RECEBIDO, response.getStatus());
        assertEquals(new BigDecimal("60.00"), response.getTotalPrice());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    @DisplayName("Deve atualizar o status do pedido para EM_PREPARO com sucesso")
    void shouldUpdateOrderStatusSuccessfully() {
        when(orderRepository.findById(100L)).thenReturn(Optional.of(mockOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(mockOrder);

        OrderResponse response = orderService.updateOrderStatus(100L, OrderStatus.EM_PREPARO, "admin@foody.com");

        assertNotNull(response);
        assertEquals(OrderStatus.EM_PREPARO, mockOrder.getStatus());
        verify(orderRepository, times(1)).save(mockOrder);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar alterar o status de um pedido já ENTREGUE")
    void shouldThrowExceptionWhenUpdatingStatusOfEntregueOrder() {
        mockOrder.setStatus(OrderStatus.ENTREGUE);
        when(orderRepository.findById(100L)).thenReturn(Optional.of(mockOrder));

        InvalidOrderStatusException exception = assertThrows(
                InvalidOrderStatusException.class,
                () -> orderService.updateOrderStatus(100L, OrderStatus.EM_PREPARO, "admin@foody.com")
        );

        assertTrue(exception.getMessage().contains("Não é possível alterar o status do pedido"));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar um pedido por ID inexistente")
    void shouldThrowExceptionWhenOrderNotFound() {
        when(orderRepository.findById(999L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> orderService.getOrderById(999L)
        );

        assertEquals("Pedido não encontrado com o ID: 999", exception.getMessage());
    }
}

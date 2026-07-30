package com.foody.delivery.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do cliente é obrigatório")
    @Column(name = "customer_name", nullable = false)
    private String customerName;

    @NotBlank(message = "O endereço de entrega é obrigatório")
    @Column(name = "delivery_address", nullable = false, length = 500)
    private String deliveryAddress;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private OrderStatus status = OrderStatus.RECEBIDO;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OrderBy("createdAt DESC")
    @Builder.Default
    private List<OrderStatusHistory> history = new ArrayList<>();

    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal totalPrice = BigDecimal.ZERO;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        calculateTotalPrice();
        if (history.isEmpty()) {
            addHistoryEntry(null, this.status, "Pedido criado com sucesso", "SISTEMA");
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        calculateTotalPrice();
    }

    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
        calculateTotalPrice();
    }

    public void removeItem(OrderItem item) {
        items.remove(item);
        item.setOrder(null);
        calculateTotalPrice();
    }

    public void calculateTotalPrice() {
        if (items != null && !items.isEmpty()) {
            this.totalPrice = items.stream()
                    .map(OrderItem::calculateSubTotal)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        } else {
            this.totalPrice = BigDecimal.ZERO;
        }
    }

    public void addHistoryEntry(OrderStatus previousStatus, OrderStatus newStatus, String description, String updatedBy) {
        OrderStatusHistory historyEntry = OrderStatusHistory.builder()
                .order(this)
                .previousStatus(previousStatus)
                .newStatus(newStatus)
                .description(description)
                .updatedBy(updatedBy != null ? updatedBy : "SISTEMA")
                .build();
        this.history.add(historyEntry);
    }
}

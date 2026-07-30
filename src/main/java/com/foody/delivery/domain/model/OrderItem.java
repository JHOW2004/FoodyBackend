package com.foody.delivery.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "tb_order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    @JsonIgnore
    private Order order;

    @NotBlank(message = "O nome do produto é obrigatório")
    @Column(name = "product_name", nullable = false)
    private String productName;

    @NotNull(message = "A quantidade é obrigatória")
    @Min(value = 1, message = "A quantidade mínima deve ser 1")
    @Column(nullable = false)
    private Integer quantity;

    @NotNull(message = "O preço unitário é obrigatório")
    @DecimalMin(value = "0.01", message = "O preço unitário deve ser maior que zero")
    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "sub_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal subTotal;

    /**
     * Calcula o subtotal do item (quantidade * preço unitário).
     */
    public BigDecimal calculateSubTotal() {
        if (unitPrice != null && quantity != null) {
            this.subTotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
        } else {
            this.subTotal = BigDecimal.ZERO;
        }
        return this.subTotal;
    }
}

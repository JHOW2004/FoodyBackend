package com.foody.delivery.order.dto;

import com.foody.delivery.domain.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusHistoryResponse {

    private Long id;
    private OrderStatus previousStatus;
    private OrderStatus newStatus;
    private String description;
    private String updatedBy;
    private LocalDateTime createdAt;
}

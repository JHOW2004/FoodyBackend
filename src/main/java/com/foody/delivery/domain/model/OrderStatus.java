package com.foody.delivery.domain.model;

/**
 * Enumeração representando os status possíveis de um pedido na Foody Delivery.
 */
public enum OrderStatus {
    RECEBIDO,
    EM_PREPARO,
    SAIU_PARA_ENTREGA,
    ENTREGUE,
    CANCELADO;

    /**
     * Verifica se a transição para um novo status é permitida.
     */
    public boolean canTransitionTo(OrderStatus newStatus) {
        if (this == CANCELADO || this == ENTREGUE) {
            return false; // Status finais não podem ser alterados
        }
        return true;
    }
}

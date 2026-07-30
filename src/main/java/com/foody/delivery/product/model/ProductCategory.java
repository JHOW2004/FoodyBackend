package com.foody.delivery.product.model;

public enum ProductCategory {
    HAMBURGUER("Hambúrgueres"),
    CACHORRO_QUENTE("Cachorros Quentes"),
    SANDUICHE("Sanduíches"),
    BEBIDA("Bebidas"),
    SOBREMESA("Sobremesas");

    private final String displayName;

    ProductCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

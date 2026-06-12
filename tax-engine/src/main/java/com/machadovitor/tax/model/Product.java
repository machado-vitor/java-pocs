package com.machadovitor.tax.model;

public record Product(String sku, String name, ProductCategory category, Money unitPrice) {

    public Product {
        sku = requireText(sku, "sku");
        name = requireText(name, "name");
    }

    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return value.strip();
    }
}

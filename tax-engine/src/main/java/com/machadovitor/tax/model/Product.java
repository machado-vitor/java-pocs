package com.machadovitor.tax.model;

import java.util.Objects;

public record Product(String sku, String name, ProductCategory category, Money unitPrice) {

    public Product {
        sku = requireText(sku, "sku");
        name = requireText(name, "name");
        Objects.requireNonNull(category, "category must not be null");
        Objects.requireNonNull(unitPrice, "unitPrice must not be null");
    }

    private static String requireText(String value, String field) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(field + " must not be blank");
        }
        return value.strip();
    }
}

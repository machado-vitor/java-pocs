package com.machadovitor.tax.rate;

import com.machadovitor.tax.model.ProductCategory;
import com.machadovitor.tax.model.UsState;

import java.util.Objects;

public record TaxRule(UsState state, ProductCategory category, int effectiveYear, TaxRate rate) {

    public TaxRule {
        Objects.requireNonNull(state, "state must not be null");
        Objects.requireNonNull(category, "category must not be null");
        Objects.requireNonNull(rate, "rate must not be null");
        if (effectiveYear < 1900) {
            throw new IllegalArgumentException("effectiveYear looks wrong: " + effectiveYear);
        }
    }

    public RuleKey key() {
        return new RuleKey(state, category);
    }

    public record RuleKey(UsState state, ProductCategory category) {
        public RuleKey {
            Objects.requireNonNull(state, "state must not be null");
            Objects.requireNonNull(category, "category must not be null");
        }
    }
}

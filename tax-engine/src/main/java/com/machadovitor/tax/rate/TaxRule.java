package com.machadovitor.tax.rate;

import com.machadovitor.tax.model.ProductCategory;
import com.machadovitor.tax.model.UsState;

public record TaxRule(UsState state, ProductCategory category, int effectiveYear, TaxRate rate) {

    public TaxRule {
        if (effectiveYear < 1900) {
            throw new IllegalArgumentException("effectiveYear looks wrong: " + effectiveYear);
        }
    }

    public RuleKey key() {
        return new RuleKey(state, category);
    }

    public record RuleKey(UsState state, ProductCategory category) {
    }
}

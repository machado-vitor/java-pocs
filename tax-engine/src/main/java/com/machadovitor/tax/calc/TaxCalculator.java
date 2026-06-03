package com.machadovitor.tax.calc;

import com.machadovitor.tax.model.Money;
import com.machadovitor.tax.model.Product;
import com.machadovitor.tax.model.UsState;
import com.machadovitor.tax.rate.TaxRate;
import com.machadovitor.tax.rate.TaxRuleRepository;

import java.math.BigDecimal;
import java.util.Objects;

public final class TaxCalculator {

    private final TaxRuleRepository repository;

    public TaxCalculator(TaxRuleRepository repository) {
        this.repository = Objects.requireNonNull(repository, "repository must not be null");
    }

    public TaxAssessment assess(Product product, int quantity, UsState state, int year) {
        Objects.requireNonNull(product, "product must not be null");
        Objects.requireNonNull(state, "state must not be null");
        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity must be positive: " + quantity);
        }

        TaxRate rate = repository.findRate(state, product.category(), year)
                .orElseThrow(() -> new TaxRuleNotFoundException(state, product.category(), year));

        Money subtotal = product.unitPrice().times(BigDecimal.valueOf(quantity));
        Money tax = switch (rate) {
            case TaxRate.Exempt ignored -> Money.zero();
            case TaxRate.Percentage percentage -> subtotal.times(percentage.asFraction());
        };

        return new TaxAssessment(product, quantity, state, year, rate, subtotal, tax);
    }
}

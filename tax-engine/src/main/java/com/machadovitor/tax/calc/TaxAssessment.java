package com.machadovitor.tax.calc;

import com.machadovitor.tax.model.Money;
import com.machadovitor.tax.model.Product;
import com.machadovitor.tax.model.UsState;
import com.machadovitor.tax.rate.TaxRate;

public record TaxAssessment(
        Product product,
        int quantity,
        UsState state,
        int year,
        TaxRate rate,
        Money subtotal,
        Money tax) {

    public Money total() {
        return subtotal.plus(tax);
    }
}

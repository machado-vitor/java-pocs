package com.machadovitor.tax.calc;

import com.machadovitor.tax.model.ProductCategory;
import com.machadovitor.tax.model.UsState;

public class TaxRuleNotFoundException extends RuntimeException {

    public TaxRuleNotFoundException(UsState state, ProductCategory category, int year) {
        super("no tax rule for " + category + " in " + state + " for year " + year);
    }
}

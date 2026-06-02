package com.machadovitor.tax.rate;

import com.machadovitor.tax.model.ProductCategory;
import com.machadovitor.tax.model.UsState;

import java.util.Optional;

public interface TaxRuleRepository {

    Optional<TaxRate> findRate(UsState state, ProductCategory category, int year);
}

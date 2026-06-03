package com.machadovitor.tax.app;

import com.machadovitor.tax.model.Money;
import com.machadovitor.tax.model.Product;
import com.machadovitor.tax.model.ProductCategory;
import com.machadovitor.tax.rate.InMemoryTaxRuleRepository;
import com.machadovitor.tax.rate.TaxRate;
import com.machadovitor.tax.rate.TaxRule;
import com.machadovitor.tax.rate.TaxRuleRepository;

import static com.machadovitor.tax.model.ProductCategory.ELECTRONICS;
import static com.machadovitor.tax.model.ProductCategory.GROCERIES;
import static com.machadovitor.tax.model.ProductCategory.LUXURY_GOODS;
import static com.machadovitor.tax.model.ProductCategory.PRESCRIPTION_DRUGS;
import static com.machadovitor.tax.model.UsState.CALIFORNIA;
import static com.machadovitor.tax.model.UsState.NEW_YORK;
import static com.machadovitor.tax.model.UsState.OREGON;
import static com.machadovitor.tax.model.UsState.TEXAS;

final class SampleData {

    private SampleData() {
    }

    static TaxRuleRepository repository() {
        InMemoryTaxRuleRepository repository = new InMemoryTaxRuleRepository();

        repository
                .add(new TaxRule(CALIFORNIA, ELECTRONICS, 2022, TaxRate.percent("7.25")))
                .add(new TaxRule(CALIFORNIA, ELECTRONICS, 2024, TaxRate.percent("7.75")))
                .add(new TaxRule(CALIFORNIA, GROCERIES, 2022, TaxRate.exempt()))
                .add(new TaxRule(CALIFORNIA, LUXURY_GOODS, 2022, TaxRate.percent("10.00")))
                .add(new TaxRule(CALIFORNIA, PRESCRIPTION_DRUGS, 2022, TaxRate.exempt()));

        repository
                .add(new TaxRule(NEW_YORK, ELECTRONICS, 2022, TaxRate.percent("8.875")))
                .add(new TaxRule(NEW_YORK, GROCERIES, 2022, TaxRate.exempt()))
                .add(new TaxRule(NEW_YORK, LUXURY_GOODS, 2023, TaxRate.percent("12.50")))
                .add(new TaxRule(NEW_YORK, PRESCRIPTION_DRUGS, 2022, TaxRate.exempt()));

        repository
                .add(new TaxRule(TEXAS, ELECTRONICS, 2022, TaxRate.percent("6.25")))
                .add(new TaxRule(TEXAS, GROCERIES, 2022, TaxRate.exempt()))
                .add(new TaxRule(TEXAS, LUXURY_GOODS, 2022, TaxRate.percent("8.25")))
                .add(new TaxRule(TEXAS, PRESCRIPTION_DRUGS, 2022, TaxRate.exempt()));

        repository
                .add(new TaxRule(OREGON, ELECTRONICS, 2022, TaxRate.exempt()))
                .add(new TaxRule(OREGON, GROCERIES, 2022, TaxRate.exempt()))
                .add(new TaxRule(OREGON, LUXURY_GOODS, 2022, TaxRate.exempt()));

        return repository;
    }

    static Product laptop() {
        return new Product("ELEC-001", "Laptop", ELECTRONICS, Money.of("1299.00"));
    }

    static Product milk() {
        return new Product("GROC-001", "Milk", GROCERIES, Money.of("3.49"));
    }

    static Product watch() {
        return new Product("LUX-001", "Designer Watch", LUXURY_GOODS, Money.of("4500.00"));
    }

    static Product insulin() {
        return new Product("RX-001", "Insulin", PRESCRIPTION_DRUGS, Money.of("85.00"));
    }
}

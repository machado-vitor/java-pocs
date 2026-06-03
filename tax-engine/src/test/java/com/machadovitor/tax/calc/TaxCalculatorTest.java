package com.machadovitor.tax.calc;

import com.machadovitor.tax.model.Money;
import com.machadovitor.tax.model.Product;
import com.machadovitor.tax.model.ProductCategory;
import com.machadovitor.tax.model.UsState;
import com.machadovitor.tax.rate.InMemoryTaxRuleRepository;
import com.machadovitor.tax.rate.TaxRate;
import com.machadovitor.tax.rate.TaxRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TaxCalculatorTest {

    private TaxCalculator calculator;

    private static final Product LAPTOP =
            new Product("ELEC-001", "Laptop", ProductCategory.ELECTRONICS, Money.of("1000.00"));
    private static final Product MILK =
            new Product("GROC-001", "Milk", ProductCategory.GROCERIES, Money.of("3.50"));

    @BeforeEach
    void setUp() {
        InMemoryTaxRuleRepository repository = new InMemoryTaxRuleRepository();
        repository
                .add(new TaxRule(UsState.CALIFORNIA, ProductCategory.ELECTRONICS, 2022, TaxRate.percent("7.25")))
                .add(new TaxRule(UsState.CALIFORNIA, ProductCategory.ELECTRONICS, 2024, TaxRate.percent("7.75")))
                .add(new TaxRule(UsState.CALIFORNIA, ProductCategory.GROCERIES, 2022, TaxRate.exempt()));
        calculator = new TaxCalculator(repository);
    }

    @Test
    void appliesPercentageRate() {
        TaxAssessment assessment = calculator.assess(LAPTOP, 1, UsState.CALIFORNIA, 2023);

        assertEquals(Money.of("1000.00"), assessment.subtotal());
        assertEquals(Money.of("72.50"), assessment.tax());
        assertEquals(Money.of("1072.50"), assessment.total());
    }

    @Test
    void multipliesByQuantity() {
        TaxAssessment assessment = calculator.assess(LAPTOP, 3, UsState.CALIFORNIA, 2023);

        assertEquals(Money.of("3000.00"), assessment.subtotal());
        assertEquals(Money.of("217.50"), assessment.tax());
    }

    @Test
    void exemptCategoryIsNotTaxed() {
        TaxAssessment assessment = calculator.assess(MILK, 4, UsState.CALIFORNIA, 2024);

        assertTrue(assessment.tax().isZero());
        assertEquals(assessment.subtotal(), assessment.total());
    }

    @Test
    void usesTheRateEffectiveForTheRequestedYear() {
        TaxAssessment before = calculator.assess(LAPTOP, 1, UsState.CALIFORNIA, 2023);
        TaxAssessment after = calculator.assess(LAPTOP, 1, UsState.CALIFORNIA, 2025);

        assertEquals(Money.of("72.50"), before.tax());
        assertEquals(Money.of("77.50"), after.tax());
    }

    @Test
    void carriesTheLatestRateForwardToFutureYears() {
        TaxAssessment assessment = calculator.assess(LAPTOP, 1, UsState.CALIFORNIA, 2030);

        assertEquals(Money.of("77.50"), assessment.tax());
    }

    @Test
    void rejectsYearsBeforeAnyRuleExists() {
        assertThrows(TaxRuleNotFoundException.class,
                () -> calculator.assess(LAPTOP, 1, UsState.CALIFORNIA, 2020));
    }

    @Test
    void rejectsStateWithoutRules() {
        assertThrows(TaxRuleNotFoundException.class,
                () -> calculator.assess(LAPTOP, 1, UsState.FLORIDA, 2024));
    }

    @Test
    void rejectsNonPositiveQuantity() {
        assertThrows(IllegalArgumentException.class,
                () -> calculator.assess(LAPTOP, 0, UsState.CALIFORNIA, 2024));
    }
}

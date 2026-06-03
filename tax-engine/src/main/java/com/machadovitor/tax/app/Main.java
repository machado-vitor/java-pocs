package com.machadovitor.tax.app;

import com.machadovitor.tax.calc.TaxAssessment;
import com.machadovitor.tax.calc.TaxCalculator;
import com.machadovitor.tax.model.UsState;
import com.machadovitor.tax.rate.TaxRuleRepository;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        TaxRuleRepository repository = SampleData.repository();
        TaxCalculator calculator = new TaxCalculator(repository);

        List<TaxAssessment> assessments = List.of(
                calculator.assess(SampleData.laptop(), 1, UsState.CALIFORNIA, 2023),
                calculator.assess(SampleData.laptop(), 1, UsState.CALIFORNIA, 2024),
                calculator.assess(SampleData.laptop(), 1, UsState.OREGON, 2024),
                calculator.assess(SampleData.milk(), 4, UsState.NEW_YORK, 2024),
                calculator.assess(SampleData.watch(), 1, UsState.NEW_YORK, 2024),
                calculator.assess(SampleData.insulin(), 2, UsState.TEXAS, 2024));

        for (TaxAssessment a : assessments) {
            System.out.println("%s in %s (%d): tax %s, total %s".formatted(
                    a.product().name(), a.state().code(), a.year(), a.tax(), a.total()));
        }
    }
}

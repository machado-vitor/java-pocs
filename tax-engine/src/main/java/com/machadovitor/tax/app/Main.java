package com.machadovitor.tax.app;

import com.machadovitor.tax.calc.TaxAssessment;
import com.machadovitor.tax.calc.TaxCalculator;
import com.machadovitor.tax.model.UsState;
import com.machadovitor.tax.rate.TaxRuleRepository;

import java.util.List;

public class Main {

    static void main() {
        TaxRuleRepository repository = SampleData.repository();
        TaxCalculator calculator = new TaxCalculator(repository);

        List<TaxAssessment> assessments = List.of(
                calculator.assess(SampleData.laptop(), 1, UsState.CALIFORNIA, 2023),
                calculator.assess(SampleData.laptop(), 1, UsState.CALIFORNIA, 2024),
                calculator.assess(SampleData.laptop(), 1, UsState.OREGON, 2024),
                calculator.assess(SampleData.milk(), 4, UsState.NEW_YORK, 2024),
                calculator.assess(SampleData.watch(), 1, UsState.NEW_YORK, 2024),
                calculator.assess(SampleData.insulin(), 2, UsState.TEXAS, 2024),
                calculator.assess(SampleData.watch(), 1, UsState.CALIFORNIA, 2025),
                calculator.assess(SampleData.watch(), 10, UsState.CALIFORNIA, 2025));

        for (TaxAssessment a : assessments) {
            System.out.printf(
                    "%s in %s (%d): tax %s, total %s%n",
                    a.product().name(), a.state().code(), a.year(), a.tax(), a.total()
            );
        }
    }
}

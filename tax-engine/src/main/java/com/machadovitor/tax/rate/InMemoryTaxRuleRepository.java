package com.machadovitor.tax.rate;

import com.machadovitor.tax.model.ProductCategory;
import com.machadovitor.tax.model.UsState;

import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.TreeMap;

public final class InMemoryTaxRuleRepository implements TaxRuleRepository {

    private final Map<TaxRule.RuleKey, NavigableMap<Integer, TaxRate>> timelines = new HashMap<>();

    public InMemoryTaxRuleRepository add(TaxRule rule) {
        NavigableMap<Integer, TaxRate> timeline =
                timelines.computeIfAbsent(rule.key(), key -> new TreeMap<>());
        TaxRate previous = timeline.putIfAbsent(rule.effectiveYear(), rule.rate());
        if (previous != null) {
            throw new IllegalStateException(
                    "duplicate rule for " + rule.key() + " in " + rule.effectiveYear());
        }
        return this;
    }

    @Override
    public Optional<TaxRate> findRate(UsState state, ProductCategory category, int year) {
        NavigableMap<Integer, TaxRate> timeline = timelines.get(new TaxRule.RuleKey(state, category));
        if (timeline == null) {
            return Optional.empty();
        }
        Map.Entry<Integer, TaxRate> effective = timeline.floorEntry(year);
        return Optional.ofNullable(effective).map(Map.Entry::getValue);
    }
}

package com.machadovitor.designpatterns.strategy.shipping;

import com.machadovitor.designpatterns.strategy.model.Money;
import com.machadovitor.designpatterns.strategy.model.Shipment;

// One interchangeable shipping algorithm; Checkout swaps between them at runtime.
// kept open rather than sealed so a new one just implements it
@FunctionalInterface
public interface ShippingStrategy {

    Money quote(Shipment shipment);

    default String name() {
        return getClass().getSimpleName();
    }

    static ShippingStrategy flatRate(String fee) {
        return new FlatRateShipping(Money.of(fee));
    }

    static ShippingStrategy weightBased(String base, String perKg) {
        return new WeightBasedShipping(Money.of(base), Money.of(perKg));
    }

    static ShippingStrategy distanceBased(String base, String perKm) {
        return new DistanceBasedShipping(Money.of(base), Money.of(perKm));
    }

    static ShippingStrategy freeOver(String threshold, ShippingStrategy otherwise) {
        return new FreeOverThresholdShipping(Money.of(threshold), otherwise);
    }
}

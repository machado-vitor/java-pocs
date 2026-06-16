package com.machadovitor.designpatterns.strategy.shipping;

import com.machadovitor.designpatterns.strategy.model.Money;
import com.machadovitor.designpatterns.strategy.model.Shipment;

// base plus a rate per kilo
public record WeightBasedShipping(Money base, Money perKg) implements ShippingStrategy {

    @Override
    public Money quote(Shipment shipment) {
        return base.plus(perKg.times(shipment.weightKg()));
    }

    @Override
    public String name() {
        return "weight " + base + " + " + perKg + "/kg";
    }
}

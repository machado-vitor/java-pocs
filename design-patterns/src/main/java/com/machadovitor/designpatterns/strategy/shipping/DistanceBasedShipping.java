package com.machadovitor.designpatterns.strategy.shipping;

import com.machadovitor.designpatterns.strategy.model.Money;
import com.machadovitor.designpatterns.strategy.model.Shipment;

import java.math.BigDecimal;

public record DistanceBasedShipping(Money base, Money perKm) implements ShippingStrategy {

    @Override
    public Money quote(Shipment shipment) {
        return base.plus(perKm.times(BigDecimal.valueOf(shipment.distanceKm())));
    }

    @Override
    public String name() {
        return "distance " + base + " + " + perKm + "/km";
    }
}

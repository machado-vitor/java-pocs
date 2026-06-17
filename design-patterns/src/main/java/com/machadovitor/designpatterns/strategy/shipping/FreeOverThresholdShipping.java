package com.machadovitor.designpatterns.strategy.shipping;

import com.machadovitor.designpatterns.strategy.model.Money;
import com.machadovitor.designpatterns.strategy.model.Shipment;

// Free once the order clears the threshold, otherwise it defers to the wrapped strategy.
public record FreeOverThresholdShipping(Money threshold, ShippingStrategy otherwise) implements ShippingStrategy {

    @Override
    public Money quote(Shipment shipment) {
        if (shipment.orderTotal().isAtLeast(threshold)) {
            return Money.zero();
        }
        return otherwise.quote(shipment);
    }

    @Override
    public String name() {
        return "free over " + threshold + " else " + otherwise.name();
    }
}

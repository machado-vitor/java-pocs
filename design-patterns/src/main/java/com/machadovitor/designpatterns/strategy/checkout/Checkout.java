package com.machadovitor.designpatterns.strategy.checkout;

import com.machadovitor.designpatterns.strategy.model.Money;
import com.machadovitor.designpatterns.strategy.model.Shipment;
import com.machadovitor.designpatterns.strategy.shipping.ShippingStrategy;

// Holds the active strategy and delegates pricing to it. never branches on which kind it is
public final class Checkout {

    private ShippingStrategy strategy;

    public Checkout(ShippingStrategy strategy) {
        this.strategy = strategy;
    }

    // swapping the strategy while running is the whole point
    public void useStrategy(ShippingStrategy strategy) {
        this.strategy = strategy;
    }

    public ShippingStrategy strategy() {
        return strategy;
    }

    public Money shippingFor(Shipment shipment) {
        return strategy.quote(shipment);
    }

    public Money totalFor(Shipment shipment) {
        return shipment.orderTotal().plus(strategy.quote(shipment));
    }
}

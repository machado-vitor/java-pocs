package com.machadovitor.tax.rate;

import java.math.BigDecimal;
import java.util.Objects;

public sealed interface TaxRate permits TaxRate.Percentage, TaxRate.Exempt {

    BigDecimal asFraction();

    record Percentage(BigDecimal percent) implements TaxRate {

        public Percentage {
            Objects.requireNonNull(percent, "percent must not be null");
            if (percent.signum() < 0) {
                throw new IllegalArgumentException("tax percent must not be negative: " + percent);
            }
        }

        @Override
        public BigDecimal asFraction() {
            return percent.movePointLeft(2);
        }

        @Override
        public String toString() {
            return percent.stripTrailingZeros().toPlainString() + "%";
        }
    }

    record Exempt() implements TaxRate {

        @Override
        public BigDecimal asFraction() {
            return BigDecimal.ZERO;
        }

        @Override
        public String toString() {
            return "exempt";
        }
    }

    static TaxRate percent(String percent) {
        return new Percentage(new BigDecimal(percent));
    }

    static TaxRate exempt() {
        return new Exempt();
    }
}

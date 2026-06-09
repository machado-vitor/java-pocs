package com.machadovitor.tax.rate;

import java.math.BigDecimal;
import java.util.Objects;

public sealed interface TaxRate permits TaxRate.Percentage, TaxRate.Exempt { // Sealed to exactly Percentage and Exempt
// the closed set lets the tax-calculation switch be checked exhaustively at compile time.
    // at compile time: javac knows the complete set, so it can:
    // - prove a switch is exhaustive
    // - reject any unauthorized implement
    // - break the build if you add a variant and forget to handle it somewhere
    // at runtime: the permitted list is baked into the .class file as a PermittedSubclasses attribute.
    //The JVM re-checks it at class-load time: a class trying to implement a
    // sealed type without being on the list fails to load (IncompatibleClassChangeError). So the seal holds even against bytecode that never went through your compiler.

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

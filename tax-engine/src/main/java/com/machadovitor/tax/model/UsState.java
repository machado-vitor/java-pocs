package com.machadovitor.tax.model;

public enum UsState {
    CALIFORNIA("CA"),
    NEW_YORK("NY"),
    TEXAS("TX"),
    FLORIDA("FL"),
    ILLINOIS("IL"),
    OREGON("OR");

    private final String code;

    UsState(String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }
}

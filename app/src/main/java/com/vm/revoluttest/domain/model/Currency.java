package com.vm.revoluttest.domain.model;

public enum Currency {
    GBP("GBP"), EUR("EUR"), USD("USD");
    private final String name;
    Currency(final String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
}

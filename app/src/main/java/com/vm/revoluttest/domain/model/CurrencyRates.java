package com.vm.revoluttest.domain.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.EnumMap;

@Data
public class CurrencyRates {
    private Currency base;
    @SerializedName("date")
    public String date;
    @SerializedName("rates")
    EnumMap<Currency, Double> rates;

    public CurrencyRates() {
    }

    protected boolean canEqual(Object other) {
        return other instanceof CurrencyRates;
    }

}

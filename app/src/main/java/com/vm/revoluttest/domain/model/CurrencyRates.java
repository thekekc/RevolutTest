package com.vm.revoluttest.domain.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import java.math.BigDecimal;
import java.util.EnumMap;

@Data
public class CurrencyRates {
    private Currency base;
    @SerializedName("date")
    private String date;
    @SerializedName("rates")
    EnumMap<Currency, BigDecimal> rates;
}

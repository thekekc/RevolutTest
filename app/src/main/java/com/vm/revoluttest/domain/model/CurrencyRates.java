package com.vm.revoluttest.domain.model;

import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.Map;

import lombok.Data;

@Data
public class CurrencyRates {
    private String base;
    @SerializedName("date")
    private String date;
    @SerializedName("rates")
    Map<String, BigDecimal> rates;

}

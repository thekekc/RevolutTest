package com.vm.revoluttest.ui.currency_exchange.currency_list;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Currency;

@Data
@Builder
public class CurrencyRatesUi {
    private Currency baseCurrency;
    private Currency exchangeCurrency;
    private BigDecimal amount;
    private BigDecimal rate;
    private String specialSymbol;
}

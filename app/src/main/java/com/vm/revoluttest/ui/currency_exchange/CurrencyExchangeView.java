package com.vm.revoluttest.ui.currency_exchange;

import com.vm.revoluttest.ui.currency_exchange.currency_list.CurrencyRatesUi;

import java.util.List;

public interface CurrencyExchangeView {
    void setToolbarRate(String string);
    void setBaseRates(List<CurrencyRatesUi> list);
    void updateBaseRates(List<CurrencyRatesUi> list);
    void updateExchangeRates(List<CurrencyRatesUi> list);
    void setExchangeRates(List<CurrencyRatesUi> list);
    void showReconnect();
    void hideReconnect();
}

package com.vm.revoluttest.ui.currency_exchange;

public interface CurrencyExchangePresenter {
    void onStart();

    void onStop();

    void onBaseCurrencyScroll(int position);

    void onExchangeScroll(int position);

    void onBaseAmountEntered(String value, int position);

    void onExchangeAmountEntered(String value, int position);
}

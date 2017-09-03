package com.vm.revoluttest.ui.currency_exchange;

import com.vm.revoluttest.domain.model.Currency;
import com.vm.revoluttest.domain.model.CurrencyRates;
import com.vm.revoluttest.ui.base.BaseCallback;
import com.vm.revoluttest.ui.currency_exchange.currency_list.CurrencyRatesUi;
import com.vm.revoluttest.ui.utils.BalanceFormatter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class CurrencyExchangePresenterImpl implements CurrencyExchangePresenter {
    private EnumMap<Currency, CurrencyRates> rates = new EnumMap<>(Currency.class);
    private CurrencyInteractor currencyInteractor;
    private CurrencyExchangeView view;
    private List<CurrencyRatesUi> baseRates;
    private List<CurrencyRatesUi> exchangeRates;
    private boolean exchangeDirection = true;
    private BigDecimal amount = BigDecimal.ZERO;
    private Currency baseCurrency = Currency.GBP;
    private Currency exchangeCurrency = Currency.GBP;

    public CurrencyExchangePresenterImpl(CurrencyInteractor currencyInteractor, CurrencyExchangeView view) {
        this.currencyInteractor = currencyInteractor;
        this.view = view;
        baseRates = new ArrayList<>();
        exchangeRates = new ArrayList<>();
        fillInRatesUi(baseRates, BigDecimal.ZERO);
        fillInRatesUi(exchangeRates, BigDecimal.ZERO);
        view.setBaseRates(baseRates);
        view.setExchangeRates(baseRates);
    }

    @Override
    public void onStart() {
        if (currencyInteractor != null) {
            currencyInteractor.onStart();
            currencyInteractor.getRates(new BaseCallback<CurrencyRates>() {
                @Override
                public void onNext(CurrencyRates currencyRates) {
                    view.hideReconnect();
                    if (currencyRates != null) {
                        rates.put(currencyRates.getBase(), currencyRates);
                        if (exchangeDirection) {
                            updateExchangeRates();
                        } else {
                            updateBaseRates();
                        }
                    }
                }

                @Override
                public void onError(Throwable t) {
                    view.showReconnect();
                }
            });
        }
    }

    @Override
    public void onStop() {
        currencyInteractor.onStop();
    }

    private void fillInRatesUi(List<CurrencyRatesUi> rates, BigDecimal amount) {
        rates.clear();
        CurrencyRates currencyRates = this.rates.get(baseCurrency);
        for (Currency currency : Currency.values()) {
            BigDecimal rate = BigDecimal.ZERO;
            if (currencyRates != null &&
                    currencyRates.getRates() != null &&
                    currencyRates.getRates().get(currency) != null) {
                rate = currencyRates.getRates().get(currency);
            }
            rates.add(CurrencyRatesUi.builder()
                    .amount(currency == baseCurrency ? BigDecimal.ZERO : amount.multiply(rate))
                    .baseCurrency(java.util.Currency.getInstance(currency.getName()))
                    .exchangeCurrency(java.util.Currency.getInstance(baseCurrency.getName()))
                    .rate(rate)
                    .build());
        }
    }

    private void updateToolbarRates() {
        CurrencyRates exchangeRates = this.rates.get(exchangeCurrency);
        if (exchangeRates != null) {
            BigDecimal backRate = exchangeRates.getRates().get(baseCurrency);
            String currencySymbolBase = java.util.Currency.getInstance(exchangeCurrency.getName()).getSymbol();
            String currencySymbolExchange = java.util.Currency.getInstance(baseCurrency.getName()).getSymbol();
            if (backRate != null) {
                view.setToolbarRate(getToolbarString(currencySymbolBase, currencySymbolExchange, backRate));
            } else {
                view.setToolbarRate("");
            }
        }
    }
    private String getToolbarString(String symbolBase, String symbolExchange, BigDecimal rate){
            return "1" + symbolBase + " = " +
                    BalanceFormatter.formatBalance4Decimals(rate) +
                    symbolExchange;
    }

    @Override
    public void onBaseCurrencyScroll(int position) {
        if (exchangeDirection) {
            amount = BigDecimal.ZERO;
            baseCurrency = Currency.values()[position % Currency.values().length];
            fillInRatesUi(baseRates, BigDecimal.ZERO);
            fillInRatesUi(exchangeRates, BigDecimal.ZERO);
            view.setExchangeRates(exchangeRates);
            view.setBaseRates(baseRates);
        } else {
            exchangeCurrency = Currency.values()[position % Currency.values().length];
        }
        updateToolbarRates();
    }

    @Override
    public void onExchangeScroll(int position) {
        if (!exchangeDirection) {
            amount = BigDecimal.ZERO;
            baseCurrency = Currency.values()[position % Currency.values().length];
            fillInRatesUi(baseRates, BigDecimal.ZERO);
            fillInRatesUi(exchangeRates, BigDecimal.ZERO);
            view.setExchangeRates(exchangeRates);
            view.setBaseRates(baseRates);
        } else {
            exchangeCurrency = Currency.values()[position % Currency.values().length];
        }
        updateToolbarRates();
    }

    @Override
    public void onBaseAmountEntered(String value, int position) {
        baseCurrency = Currency.values()[position % Currency.values().length];
        exchangeDirection = true;
        amount = new BigDecimal(value);
        fillInRatesUi(exchangeRates, amount);
        view.setExchangeRates(exchangeRates);
        updateToolbarRates();
    }

    @Override
    public void onExchangeAmountEntered(String value, int position) {
        baseCurrency = Currency.values()[position % Currency.values().length];
        exchangeDirection = false;
        amount = new BigDecimal(value);
        fillInRatesUi(baseRates, amount);
        view.setBaseRates(baseRates);
        updateToolbarRates();
    }

    private void updateBaseRates() {
        fillInRatesUi(baseRates, amount);
        view.updateBaseRates(baseRates);
    }

    private void updateExchangeRates() {
        fillInRatesUi(exchangeRates, amount);
        view.updateExchangeRates(exchangeRates);
    }

}

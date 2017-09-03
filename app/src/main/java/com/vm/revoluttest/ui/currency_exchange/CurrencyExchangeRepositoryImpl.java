package com.vm.revoluttest.ui.currency_exchange;

import com.vm.revoluttest.domain.model.Currency;
import com.vm.revoluttest.domain.model.CurrencyRates;
import com.vm.revoluttest.domain.network.CurrencyService;
import io.reactivex.Observable;

import java.util.concurrent.TimeUnit;

/**
 * Provides currency rates with interval
 */

public class CurrencyExchangeRepositoryImpl implements CurrencyExchangeRepository {
    private static final long REPEAT_PERIOD = 30000;
    private CurrencyService service;
    private long repeatMs = REPEAT_PERIOD;

    public CurrencyExchangeRepositoryImpl(CurrencyService service) {
        this.service = service;
    }

    @Override
    public Observable<CurrencyRates> getAllRates(){
       Observable<Currency> currencyRatesObservable = Observable.fromArray(Currency.values());
       return Observable.interval(0, repeatMs, TimeUnit.MILLISECONDS)
                .flatMap(n -> currencyRatesObservable)
                .flatMap(currency -> service.getCurrencyList(currency.getName()));
    }

    @Override
    public void setRepeatPeriod(long repeatMs){
        this.repeatMs = repeatMs;
    }

}

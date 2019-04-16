package com.vm.revoluttest.ui.currency_exchange;

import com.vm.revoluttest.domain.model.CurrencyRates;
import com.vm.revoluttest.domain.network.CurrencyService;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

/**
 * Provides currency rates with interval
 */

public class CurrencyExchangeRepositoryImpl implements CurrencyExchangeRepository {
    private static final long REPEAT_PERIOD = 1000;
    private CurrencyService service;
    private long repeatMs = REPEAT_PERIOD;

    public CurrencyExchangeRepositoryImpl(CurrencyService service) {
        this.service = service;
    }


    @Override
    public Observable<CurrencyRates> getRatesByBaseCurrency(String baseCurrency){

       return Observable.interval(0, repeatMs, TimeUnit.MILLISECONDS)
                .flatMap(n -> service.getCurrencyList(baseCurrency));
    }

    @Override
    public void setRepeatPeriod(long repeatMs){
        this.repeatMs = repeatMs;
    }

}

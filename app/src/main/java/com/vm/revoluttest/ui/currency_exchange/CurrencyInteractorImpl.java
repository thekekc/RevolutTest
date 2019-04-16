package com.vm.revoluttest.ui.currency_exchange;

import com.vm.revoluttest.domain.model.CurrencyRates;
import com.vm.revoluttest.ui.base.BaseCallback;
import com.vm.revoluttest.ui.base.BaseInteractor;

@SuppressWarnings("WeakerAccess")
public class CurrencyInteractorImpl extends BaseInteractor implements CurrencyInteractor {
    private CurrencyExchangeRepository repository;
    private Long delayMillis = 1000L;
    private BaseCallback<CurrencyRates> callback;

    public CurrencyInteractorImpl(CurrencyExchangeRepository repository) {
        this.repository = repository;
        repository.setRepeatPeriod(delayMillis);
    }


    @Override
    public void getRatesPeriodically(BaseCallback<CurrencyRates> callback, String baseCurrency) {
        onStop();
        onStart();
        this.callback = callback;
        subscribeAsync(repository
                .getRatesByBaseCurrency(baseCurrency)
                .doOnError(callback::onError)
                .retry(), callback::onNext, callback::onError);
    }

    @Override
    public void changeBaseCurrency(String baseCurrency){
        if(callback!=null){
            getRatesPeriodically(callback, baseCurrency);
        }
    }

}

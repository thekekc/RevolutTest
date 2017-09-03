package com.vm.revoluttest.ui.currency_exchange;

import com.vm.revoluttest.domain.model.CurrencyRates;
import com.vm.revoluttest.ui.base.BaseCallback;
import com.vm.revoluttest.ui.base.BaseInteractor;

@SuppressWarnings("WeakerAccess")
public class CurrencyInteractor extends BaseInteractor {
    private CurrencyExchangeRepository repository;

    public CurrencyInteractor(CurrencyExchangeRepository repository) {
        this.repository = repository;
    }

    public void getRates(BaseCallback<CurrencyRates> callback) {
        subscribeAsync(repository
                .getAllRates()
                .doOnError(callback::onError)
                .retry(), callback::onNext, callback::onError);
    }
}

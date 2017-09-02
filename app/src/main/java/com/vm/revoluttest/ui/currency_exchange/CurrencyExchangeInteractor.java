package com.vm.revoluttest.ui.currency_exchange;

import com.vm.revoluttest.domain.model.Currency;
import com.vm.revoluttest.domain.model.CurrencyRates;
import com.vm.revoluttest.domain.network.CurrencyService;
import com.vm.revoluttest.ui.base.BaseCallback;
import io.reactivex.Observable;


public class CurrencyExchangeInteractor {
    public void getAllRates(CurrencyService service, BaseCallback<CurrencyRates> callback){
        Observable
                .fromArray(Currency.values())
                .flatMap(currency -> {service.getCurrencyList(currency.getName())})
                .repeat(30000)
                .subscribe();
    }
}

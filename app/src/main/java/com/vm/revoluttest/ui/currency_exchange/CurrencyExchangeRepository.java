package com.vm.revoluttest.ui.currency_exchange;

import com.vm.revoluttest.domain.model.CurrencyRates;
import io.reactivex.Observable;

public interface CurrencyExchangeRepository {
    Observable<CurrencyRates> getAllRates();

    void setRepeatPeriod(long repeatMs);
}

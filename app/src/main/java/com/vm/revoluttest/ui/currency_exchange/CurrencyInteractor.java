package com.vm.revoluttest.ui.currency_exchange;

import com.vm.revoluttest.domain.model.CurrencyRates;
import com.vm.revoluttest.ui.base.BaseCallback;

interface CurrencyInteractor {
    void getRatesPeriodically(BaseCallback<CurrencyRates> callback, String baseCurrency);

    void changeBaseCurrency(String baseCurrency);
}

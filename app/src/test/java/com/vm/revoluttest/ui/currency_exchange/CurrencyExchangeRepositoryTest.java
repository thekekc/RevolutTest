package com.vm.revoluttest.ui.currency_exchange;

import android.annotation.SuppressLint;

import com.vm.revoluttest.domain.model.Currency;
import com.vm.revoluttest.domain.model.CurrencyRates;
import com.vm.revoluttest.domain.network.CurrencyService;

import org.junit.Test;

import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;

public class CurrencyExchangeRepositoryTest {


    @Test
    public void getAllRates() throws Exception {
        CurrencyExchangeRepositoryImpl currencyExchangeRepositoryImpl =
                new CurrencyExchangeRepositoryImpl(new CurrencyServiceStub());
        TestObserver<CurrencyRates> testObserver = new TestObserver<>();
        currencyExchangeRepositoryImpl.setRepeatPeriod(1000);
        currencyExchangeRepositoryImpl.getRatesByBaseCurrency("USD").take(3).subscribe(testObserver);
        testObserver.await();
        testObserver.assertNoErrors();
        testObserver.assertValueCount(3);
    }

    @SuppressLint("NewApi")
    @Test
    public void getAllRatesError() throws Exception {
        CurrencyExchangeRepositoryImpl currencyExchangeRepositoryImpl =
                new CurrencyExchangeRepositoryImpl(new CurrencyServiceErrorStub());
        TestObserver<CurrencyRates> testObserver = new TestObserver<>();
        currencyExchangeRepositoryImpl.setRepeatPeriod(1000);
        currencyExchangeRepositoryImpl.getRatesByBaseCurrency("USD").take(3).subscribe(testObserver);
        testObserver.await();
        testObserver.assertError(Objects::nonNull);
    }

    public static class CurrencyServiceStub implements CurrencyService{

        @Override
        public Observable<CurrencyRates> getCurrencyList(String baseCurrency) {
            CurrencyRates currencyRates = new CurrencyRates();
            return Observable.just(currencyRates);
        }
    }

    public static class CurrencyServiceErrorStub implements CurrencyService{

        @Override
        public Observable<CurrencyRates> getCurrencyList(String baseCurrency) {
            return Observable.error(new Throwable());
        }
    }
}
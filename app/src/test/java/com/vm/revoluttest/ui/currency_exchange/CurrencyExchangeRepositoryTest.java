package com.vm.revoluttest.ui.currency_exchange;

import android.annotation.SuppressLint;

import com.vm.revoluttest.domain.model.Currency;
import com.vm.revoluttest.domain.model.CurrencyRates;
import com.vm.revoluttest.domain.network.CurrencyService;
import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;
import org.junit.Test;

import java.util.Objects;

public class CurrencyExchangeRepositoryTest {


    @Test
    public void getAllRates() throws Exception {
        CurrencyExchangeRepositoryImpl currencyExchangeRepositoryImpl =
                new CurrencyExchangeRepositoryImpl(new CurrencyServiceStub());
        TestObserver<CurrencyRates> testObserver = new TestObserver<>();
        currencyExchangeRepositoryImpl.setRepeatPeriod(1000);
        currencyExchangeRepositoryImpl.getAllRates().take(3).subscribe(testObserver);
        //Thread.sleep(100000);
        testObserver.await();
        testObserver.assertNoErrors();
        testObserver.assertValueAt(1, currencyRates -> currencyRates.getBase()==Currency.USD);
        testObserver.assertValueCount(3);
    }

    @SuppressLint("NewApi")
    @Test
    public void getAllRatesError() throws Exception {
        CurrencyExchangeRepositoryImpl currencyExchangeRepositoryImpl =
                new CurrencyExchangeRepositoryImpl(new CurrencyServiceErrorStub());
        TestObserver<CurrencyRates> testObserver = new TestObserver<>();
        currencyExchangeRepositoryImpl.setRepeatPeriod(1000);
        currencyExchangeRepositoryImpl.getAllRates().take(3).subscribe(testObserver);
        testObserver.await();
        testObserver.assertError(Objects::nonNull);
    }

    public static class CurrencyServiceStub implements CurrencyService{

        @Override
        public Observable<CurrencyRates> getCurrencyList(String baseCurrency) {
            CurrencyRates currencyRates = new CurrencyRates();
            currencyRates.setBase(Currency.USD);
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
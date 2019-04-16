package com.vm.revoluttest.ui.currency_exchange;

import com.vm.revoluttest.domain.model.CurrencyRates;
import com.vm.revoluttest.domain.resources.ResourceRepository;
import com.vm.revoluttest.ui.base.BaseCallback;
import com.vm.revoluttest.ui.base.BaseInteractor;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.TreeMap;

@SuppressWarnings("WeakerAccess")
public class CurrencyListViewModelTest {
    CurrencyListViewModel currencyListViewModel;
    CurrencyInteractorImplStub interactorStub = new CurrencyInteractorImplStub();

    @Before
    public void init(){
        currencyListViewModel = new CurrencyListViewModel(interactorStub, new ResourceRepositoryStub());
    }


    @Test
    public void getCurrencyViewModelList() {
        interactorStub.isError = false;
        Assert.assertEquals(currencyListViewModel.showLoading.get(), true);
        currencyListViewModel.onStart();
        Assert.assertEquals(currencyListViewModel.showLoading.get(), false);
        Assert.assertEquals(currencyListViewModel.getCurrencyViewModelList().get(0).shortCurrencyName.get(), "EUR");
        Assert.assertEquals(currencyListViewModel.getCurrencyViewModelList().get(1).shortCurrencyName.get(), "AUD");
        Assert.assertEquals(currencyListViewModel.getCurrencyViewModelList().get(2).shortCurrencyName.get(), "CAD");
        Assert.assertEquals(currencyListViewModel.getCurrencyViewModelList().get(3).shortCurrencyName.get(), "EUR");
        Assert.assertEquals(currencyListViewModel.getCurrencyViewModelList().get(2).amountString.get(), "300");
    }

    @Test
    public void getCurrencyViewModelListError() {
        Assert.assertEquals(currencyListViewModel.showLoading.get(), true);
        interactorStub.isError = true;
        currencyListViewModel.onStart();
        Assert.assertEquals(currencyListViewModel.showLoading.get(), true);
    }

    @Test
    public void getClickPosition() {
        interactorStub.isError = false;
        currencyListViewModel.onStart();
        currencyListViewModel.getCurrencyViewModelList().get(1).getClickListener().onClick(currencyListViewModel.getCurrencyViewModelList().get(1), 1);
        Assert.assertEquals(currencyListViewModel.getClickPosition(), 1);
        Assert.assertEquals(interactorStub.baseCurrency, "AUD");
    }

    class CurrencyInteractorImplStub extends BaseInteractor implements CurrencyInteractor {
        String baseCurrency = "Unchanged";
        boolean isError = false;
        @Override
        public void getRatesPeriodically(BaseCallback<CurrencyRates> callback, String baseCurrency) {
            CurrencyRates currencyRates = new CurrencyRates();
            currencyRates.setBase("USD");
            currencyRates.setRates(new TreeMap<String, BigDecimal>(){{
              put("EUR", new BigDecimal(1));
              put("AUD", new BigDecimal(2));
              put("CAD", new BigDecimal(3));
            }});
            if(isError){
                callback.onError(new Throwable("sss"));
            } else {
                callback.onNext(currencyRates);
            }
        }

        @Override
        public void changeBaseCurrency(String baseCurrency) {
            this.baseCurrency = baseCurrency;

        }
    }

    class ResourceRepositoryStub implements ResourceRepository{

        @Override
        public int getDrawableIdByName(String name) {
            return 200;
        }

        @Override
        public String getStringByName(String name) {
            return "Currency name";
        }
    }
}
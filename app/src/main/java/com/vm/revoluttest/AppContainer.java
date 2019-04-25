package com.vm.revoluttest;

import android.app.Application;

import com.vm.revoluttest.domain.network.NetworkModule;
import com.vm.revoluttest.domain.network.ServiceModule;
import com.vm.revoluttest.domain.resources.ResourceRepository;
import com.vm.revoluttest.domain.resources.ResourceRepositoryImpl;
import com.vm.revoluttest.ui.currency_exchange.CurrencyExchangeRepository;
import com.vm.revoluttest.ui.currency_exchange.CurrencyExchangeRepositoryImpl;

import retrofit2.Retrofit;

public class AppContainer extends Application {
    private CurrencyExchangeRepository currencyExchangeRepository;
    private ResourceRepository resourceRepository;

    @Override
    public void onCreate() {
        super.onCreate();
        NetworkModule networkModule = new NetworkModule();
        ServiceModule serviceModule = new ServiceModule();
        Retrofit retrofit = networkModule.provideRetrofit(
                networkModule.provideOkHttpClient(networkModule.provideHttpLoggingInterceptor()),
                networkModule.provideGson(networkModule.provideCurrencyEnumMapDeserializer()));
        currencyExchangeRepository = new CurrencyExchangeRepositoryImpl(
                serviceModule.provideCurrencyService(retrofit));
        resourceRepository = new ResourceRepositoryImpl(this);
    }

    public CurrencyExchangeRepository getCurrencyRepository() {
        return currencyExchangeRepository;
    }

    public ResourceRepository getResourceRepository() {
        return resourceRepository;
    }
}

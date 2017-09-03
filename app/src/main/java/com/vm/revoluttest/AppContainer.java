package com.vm.revoluttest;

import android.app.Application;
import com.vm.revoluttest.domain.network.NetworkModule;
import com.vm.revoluttest.domain.network.ServiceModule;
import com.vm.revoluttest.ui.currency_exchange.CurrencyExchangeRepository;
import com.vm.revoluttest.ui.currency_exchange.CurrencyExchangeRepositoryImpl;
import retrofit2.Retrofit;

public class AppContainer extends Application {
    private CurrencyExchangeRepository repository;

    @Override
    public void onCreate() {
        super.onCreate();
        NetworkModule networkModule = new NetworkModule();
        ServiceModule serviceModule = new ServiceModule();
        Retrofit retrofit = networkModule.provideRetrofit(
                networkModule.provideOkHttpClient(networkModule.provideHttpLoggingInterceptor()),
                networkModule.provideGson(networkModule.provideCurrencyEnumMapDeserializer()));
        repository = new CurrencyExchangeRepositoryImpl(serviceModule.provideCurrencyService(retrofit));
    }

    public CurrencyExchangeRepository getCurrencyRepository(){
        return repository;
    }
}

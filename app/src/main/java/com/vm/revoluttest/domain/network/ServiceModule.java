package com.vm.revoluttest.domain.network;

import retrofit2.Retrofit;

public class ServiceModule {
    public CurrencyService provideCurrencyService(Retrofit retrofit){
        return retrofit.create(CurrencyService.class);
    }
}

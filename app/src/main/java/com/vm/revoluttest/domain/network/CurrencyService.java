package com.vm.revoluttest.domain.network;


import com.vm.revoluttest.domain.model.CurrencyRates;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CurrencyService {
    @GET("latest")
    Observable<CurrencyRates> getCurrencyList(@Query("base") String baseCurrency);
}

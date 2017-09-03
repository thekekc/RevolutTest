package com.vm.revoluttest.domain.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.vm.revoluttest.BuildConfig;
import com.vm.revoluttest.domain.gson.CurrencyEnumMapDeserializer;
import com.vm.revoluttest.domain.model.Currency;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.concurrent.TimeUnit;

public class NetworkModule {

    private static final int NETWORK_TIMEOUT = 10; //sec

    public Retrofit provideRetrofit(OkHttpClient okHttpClient, Gson gson) {
        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public Gson provideGson(CurrencyEnumMapDeserializer typeAdapter) {
        Type mapType = new TypeToken<EnumMap<Currency, BigDecimal>>() {}.getType();
        return new GsonBuilder()
                .registerTypeAdapter(mapType, typeAdapter)
                .create();
    }

    public CurrencyEnumMapDeserializer provideCurrencyEnumMapDeserializer(){
        return new CurrencyEnumMapDeserializer();
    }

    public OkHttpClient provideOkHttpClient(HttpLoggingInterceptor httpLoggingInterceptor) {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS);
        builder.writeTimeout(NETWORK_TIMEOUT, TimeUnit.SECONDS);
        if(BuildConfig.ENABLE_HTTP_LOGGING){
            builder.addInterceptor(httpLoggingInterceptor);
        }

        return builder.build();
    }

    public HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        return logging;
    }
}

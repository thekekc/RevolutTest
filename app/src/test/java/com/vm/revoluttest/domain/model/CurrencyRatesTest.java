package com.vm.revoluttest.domain.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.vm.revoluttest.domain.gson.CurrencyEnumMapDeserializer;
import org.junit.Test;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.EnumMap;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("WeakerAccess")
public class CurrencyRatesTest {

    public static String json = "{\"base\":\"USD\",\"date\":\"2017-09-01\",\"rates\":{\"AUD\":1.2602,\"BGN\":1.6408,\"BRL\":3.1395,\"CAD\":1.2441,\"CHF\":0.95982,\"CNY\":6.5591,\"CZK\":21.877,\"DKK\":6.2398,\"GBP\":0.77244,\"HKD\":7.8248,\"HRK\":6.2261,\"HUF\":255.95,\"IDR\":13318.0,\"ILS\":3.5737,\"INR\":64.034,\"JPY\":110.14,\"KRW\":1120.3,\"MXN\":17.836,\"MYR\":4.2705,\"NOK\":7.7647,\"NZD\":1.3958,\"PHP\":51.091,\"PLN\":3.5576,\"RON\":3.856,\"RUB\":57.737,\"SEK\":7.9512,\"SGD\":1.3545,\"THB\":33.17,\"TRY\":3.438,\"ZAR\":12.935,\"EUR\":0.83893}}";
    public Type mapType = new TypeToken<EnumMap<Currency, BigDecimal>>() {}.getType();
    public Gson gson = new GsonBuilder().registerTypeAdapter(mapType, new CurrencyEnumMapDeserializer()).create();

    @Test
    public void parseModel(){
        CurrencyRates rates = gson.fromJson(json, CurrencyRates.class);
        assertEquals(rates.getBase(),Currency.USD);
        assertEquals(rates.getRates().get(Currency.EUR), BigDecimal.valueOf(0.83893));
        assertEquals(rates.getRates().get(Currency.GBP), BigDecimal.valueOf(0.77244));
    }


}
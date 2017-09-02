package com.vm.revoluttest.domain.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.vm.revoluttest.domain.model.Currency;

import java.lang.reflect.Type;
import java.util.EnumMap;
import java.util.Map;

public class CurrencyEnumMapDeserializer implements JsonDeserializer<EnumMap<Currency, Double>> {
    @Override
    public EnumMap<Currency, Double> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        Type mapType = new TypeToken<Map<String, Double>>() {}.getType();
        Map<String, Double> rates= context.deserialize(json,mapType);
        EnumMap<Currency, Double> convertedRates = new EnumMap<>(Currency.class);
        for (Currency currency : Currency.values()) {
            Double rate = rates.get(currency.getName());
            if(rate!=null){
                convertedRates.put(currency, rate);
            }
        }
        return convertedRates;
    }
}

package com.vm.revoluttest.domain.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.vm.revoluttest.domain.model.Currency;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.Map;

/**
 * deserializer that maps Map<String, BigDecimal> into EnumMap<Currency, BigDecimal>
 */
public class CurrencyEnumMapDeserializer implements JsonDeserializer<EnumMap<Currency, BigDecimal>> {
    @Override
    public EnumMap<Currency, BigDecimal> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        Type mapType = new TypeToken<Map<String, BigDecimal>>() {}.getType();
        Map<String, BigDecimal> rates= context.deserialize(json,mapType);
        EnumMap<Currency, BigDecimal> convertedRates = new EnumMap<>(Currency.class);
        for (Currency currency : Currency.values()) {
            BigDecimal rate = rates.get(currency.getName());
            if(rate!=null){
                convertedRates.put(currency, rate);
            }
        }
        return convertedRates;
    }
}

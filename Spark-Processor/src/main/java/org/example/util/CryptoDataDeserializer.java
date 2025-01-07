package org.example.util;

import org.apache.kafka.common.serialization.Deserializer;
import org.example.entity.CryptoData;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Date;

public class CryptoDataDeserializer implements Deserializer<CryptoData> {

    private Gson gson = new Gson();

    @Override
    public CryptoData deserialize(String topic, byte[] data) {
        JsonElement jsonElement = gson.fromJson(new String(data), JsonElement.class);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        // Parse fields manually
        String id = jsonObject.get("ticker").getAsString();
        double open = jsonObject.get("open").getAsDouble();
        double high = jsonObject.get("high").getAsDouble();
        double low = jsonObject.get("low").getAsDouble();
        double close = jsonObject.get("close").getAsDouble();
        double volume = jsonObject.get("volume").getAsDouble();
        
        // MarketCap is missing, set default value or remove from CryptoData class if unnecessary
        double marketCap = 0.0;  // Default value or you can remove the field entirely
        
        long timestampMillis = jsonObject.get("timestamp").getAsLong();
        Date timestamp = new Date(timestampMillis);  // Convert long to Date

        // Create CryptoData object
        return new CryptoData(id, open, high, low, close, volume, marketCap, timestamp);
    }
}

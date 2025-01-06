package org.example.util;

import org.apache.kafka.common.serialization.Deserializer;
import org.example.entity.CryptoData;
import com.google.gson.Gson;

public class CryptoDataDeserializer implements Deserializer<CryptoData> {

    private Gson gson = new Gson();

    @Override
    public CryptoData deserialize(String topic, byte[] data) {
        return gson.fromJson(new String(data), CryptoData.class);
    }
}

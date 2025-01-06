package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import kafka.serializer.Encoder;
import kafka.utils.VerifiableProperties;

public class CryptoDataEncoder implements Encoder<CryptoData> {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public CryptoDataEncoder(VerifiableProperties verifiableProperties) {
    }

    public byte[] toBytes(CryptoData event) {
        try {
            String msg = objectMapper.writeValueAsString(event);
            System.out.println("Serialized Data: " + msg);
            return msg.getBytes();
        } catch (JsonProcessingException e) {
            System.out.println("Error in Serialization: " + e.getMessage());
        }
        return null;
    }
}

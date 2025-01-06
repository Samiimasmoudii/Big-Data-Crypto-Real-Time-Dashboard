package org.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import org.json.JSONArray;
import org.json.JSONObject;

public class CryptoDataProducer {

    private final Producer<String, CryptoData> producer;

    public CryptoDataProducer(final Producer<String, CryptoData> producer) {
        this.producer = producer;
    }

    public static void main(String[] args) throws Exception {
        Properties properties = PropertyFileReader.readPropertyFile();
        Producer<String, CryptoData> producer = new Producer<>(new ProducerConfig(properties));
        CryptoDataProducer cryptoProducer = new CryptoDataProducer(producer);
        cryptoProducer.generateCryptoEvents(properties.getProperty("kafka.topic"), properties.getProperty("polygon.api.key"));
    }

    private void generateCryptoEvents(String topic, String apiKey) throws InterruptedException {
        System.out.println("Fetching and sending crypto events...");
        while (true) {
            CryptoData cryptoData = fetchCryptoData(apiKey);
            if (cryptoData != null) {
                System.out.println("Fetched Data: " + cryptoData);
                producer.send(new KeyedMessage<>(topic, cryptoData));
            }
            Thread.sleep(5000); // Fetch every 5 seconds
        }
    }

    private CryptoData fetchCryptoData(String apiKey) {
        try {
            String url = "https://api.polygon.io/v2/aggs/ticker/X:BTCUSD/range/1/day/2023-01-09/2023-01-09?apiKey=" + apiKey;
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Parse JSON response
            JSONObject jsonResponse = new JSONObject(response.toString());
            if ("OK".equals(jsonResponse.getString("status"))) {
                JSONArray results = jsonResponse.getJSONArray("results");
                if (results.length() > 0) {
                    JSONObject firstResult = results.getJSONObject(0);
                    return new CryptoData(
                        "BTCUSD",
                        firstResult.getDouble("o"),
                        firstResult.getDouble("c"),
                        firstResult.getDouble("h"),
                        firstResult.getDouble("l"),
                        firstResult.getDouble("v"),
                        firstResult.getLong("t")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

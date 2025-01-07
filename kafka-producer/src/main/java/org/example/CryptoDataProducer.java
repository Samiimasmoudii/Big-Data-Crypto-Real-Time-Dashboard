package org.example;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
            // Get current date in the required format (YYYY-MM-DD)
            String currentDate = getYesterdayDate();
            
            // Generate the URL with dynamic date
            String url = "https://api.polygon.io/v2/aggs/ticker/X:BTCUSD/range/1/minute/" + currentDate + "/" + currentDate + "?apiKey=" + apiKey;
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            int responseCode = connection.getResponseCode();

            // Handle rate-limiting with HTTP status 429
            if (responseCode == 429) {
                System.out.println("Rate limit exceeded. Sleeping for 5 seconds...");
                Thread.sleep(5000); // Sleep for 5 seconds
                return fetchCryptoData(apiKey); // Retry the request after the delay
            }

            // Read the response
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

                    // Convert the timestamp (already in milliseconds)
                    long timestamp = firstResult.getLong("t");

                    // Convert timestamp to Date
                    java.util.Date date = new java.util.Date(timestamp);

                    // Return a new CryptoData object
                    return new CryptoData(
                            "BTCUSD",
                            firstResult.getDouble("o"),
                            firstResult.getDouble("c"),
                            firstResult.getDouble("h"),
                            firstResult.getDouble("l"),
                            firstResult.getDouble("v"),
                            date // Use the Date object directly
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Method to get the current date formatted as YYYY-MM-DD
    private String getYesterdayDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  // Correct format for the API request
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -1);  // Subtract 1 day to get yesterday's date
        return sdf.format(calendar.getTime());
    }
    
}

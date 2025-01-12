package org.example.processor;

import java.util.*;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
//import org.apache.spark.SparkContext;
//import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
//import org.apache.spark.sql.SparkSession;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.*;
import org.apache.spark.streaming.kafka010.*;

import org.example.entity.CryptoData;
import org.example.util.CryptoDataDeserializer;
import org.example.util.PropertyFileReader;

import com.datastax.spark.connector.japi.CassandraJavaUtil;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;

public class StreamProcessor {
    
   
        

    

    public static void main(String[] args) throws Exception {

        // Read configuration from properties file
        String file = "spark-crypto-processor.properties";
        Properties prop = PropertyFileReader.readPropertyFile(file);

        SparkConf conf = ProcessorUtils.getSparkConf(prop);

        JavaStreamingContext streamingContext = new JavaStreamingContext(conf, Durations.seconds(10));
        JavaSparkContext sc = streamingContext.sparkContext();

        streamingContext.checkpoint(prop.getProperty("org.example.spark.checkpoint.dir"));

        // Kafka consumer configuration for crypto data
        Map<String, Object> kafkaParams = new HashMap<>();
        kafkaParams.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, prop.getProperty("org.example.brokerlist"));
        kafkaParams.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        kafkaParams.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, CryptoDataDeserializer.class);
        kafkaParams.put(ConsumerConfig.GROUP_ID_CONFIG, prop.getProperty("org.example.topic"));
        kafkaParams.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, prop.getProperty("org.example.resetType"));
        kafkaParams.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

        Collection<String> topics = Arrays.asList(prop.getProperty("org.example.topic"));

        // Create direct stream from Kafka
        JavaInputDStream<ConsumerRecord<String, CryptoData>> stream = KafkaUtils.createDirectStream(streamingContext,
                LocationStrategies.PreferConsistent(),
                ConsumerStrategies.<String, CryptoData>Subscribe(topics, kafkaParams));

        // Extract crypto data from Kafka stream
        JavaDStream<CryptoData> cryptoDataStream = stream.map(v -> v.value());
        cryptoDataStream.print(); // Print data to console (for debugging)

        // Example transformation: Calculate average price from incoming crypto data
        JavaDStream<Double> priceStream = cryptoDataStream.map(v -> (v.getOpen() + v.getClose()) / 2); // Average price
                                                                                                       // of crypto

        // You can save the price data or apply further transformations
        priceStream.foreachRDD(rdd -> {
            if (!rdd.isEmpty()) {
                // Process the RDD (e.g., save to Cassandra or HDFS)
                System.out.println("Average Crypto Price: " + rdd.collect());
                // Optionally, save data to Cassandra or any other store
                // For example: ProcessorUtils.saveAvgToCassandra(rdd);
            }
        });
        
        List<CryptoData> testList = new ArrayList<>();
        
        testList.add(new CryptoData("BTC", 50000.0, 50500.0, 49500.0, 50200.0, 1000.0,
                new java.sql.Date(System.currentTimeMillis())));
      
        JavaRDD<CryptoData> testRDD = sc.parallelize(testList);

        CassandraJavaUtil.javaFunctions(testRDD)
                .writerBuilder("cryptodatakeyspace", "crypto_data", CassandraJavaUtil.mapToRow(CryptoData.class))
                .saveToCassandra();
        // Additional processing steps if needed (e.g., filtering, mapping, etc.)

        streamingContext.start();
        streamingContext.awaitTermination();
    }
}

package org.example.processor;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.example.entity.AverageData;
import org.example.entity.CryptoData;
import com.datastax.spark.connector.japi.CassandraJavaUtil;

import static com.datastax.spark.connector.japi.CassandraStreamingJavaUtil.javaFunctions;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

public class ProcessorUtils {

    // Get the Spark Configuration from properties
    public static SparkConf getSparkConf(Properties prop) {
        SparkConf sparkConf = new SparkConf()
                .setAppName(prop.getProperty("org.example.spark.app.name"))
                .setMaster(prop.getProperty("org.example.spark.master"))
                .set("spark.cassandra.connection.host", prop.getProperty("org.example.cassandra.host"))
                .set("spark.cassandra.connection.port", prop.getProperty("org.example.cassandra.port"))
                .set("spark.cassandra.auth.username", prop.getProperty("org.example.cassandra.username"))
                .set("spark.cassandra.auth.password", prop.getProperty("org.example.cassandra.password"))
                .set("spark.cassandra.connection.keep_alive_ms", prop.getProperty("org.example.cassandra.keep_alive"));

        // If running locally, configure Spark driver
        if ("local".equals(prop.getProperty("org.example.env"))) {
            sparkConf.set("spark.driver.bindAddress", "127.0.0.1");
        }
        return sparkConf;
    }

    // Save CryptoData to Cassandra
    public static void saveCryptoDataToCassandra(final JavaDStream<CryptoData> dataStream) {
        System.out.println("Saving CryptoData to Cassandra...");

        // Map Cassandra table columns for CryptoData
        HashMap<String, String> columnNameMappings = new HashMap<>();
        columnNameMappings.put("ticker", "ticker");
        columnNameMappings.put("timestamp", "timestamp");
        columnNameMappings.put("close", "close");
        columnNameMappings.put("high", "high");
        columnNameMappings.put("low", "low");
        columnNameMappings.put("open", "open");
        columnNameMappings.put("volume", "volume");

        // Save data to Cassandra
        javaFunctions(dataStream).writerBuilder("cryptodatakeyspace", "crypto_data",
                CassandraJavaUtil.mapToRow(CryptoData.class, columnNameMappings)).saveToCassandra();

        System.out.println(
                "================================================ SAVED CRYPTO DATA TO CASSANDRA =================================================");
    }

    // Save data to HDFS
    public static void saveDataToHDFS(final JavaDStream<CryptoData> dataStream, String saveFile, SparkSession sql) {
        System.out.println("Saving CryptoData to HDFS...");

        dataStream.foreachRDD(rdd -> {
            if (rdd.isEmpty()) {
                return;
            }
            Dataset<Row> dataFrame = sql.createDataFrame(rdd, CryptoData.class);

            // Select and save required columns for CryptoData
            Dataset<Row> dfStore = dataFrame.selectExpr("ticker", "open", "close", "high", "low", "volume",
                    "timestamp");
            dfStore.printSchema();
            dfStore.write().mode(SaveMode.Append).parquet(saveFile);
        });
    }

    // Transform a Row into a CryptoData object
    public static CryptoData transformData(Row row) {
        System.out.println(row);
        
        // Get the column values using the column names (based on your mappings)
        String ticker = row.getString(row.fieldIndex("ticker"));
        double close = row.getDouble(row.fieldIndex("close"));
        double high = row.getDouble(row.fieldIndex("high"));
        double low = row.getDouble(row.fieldIndex("low"));
        double open = row.getDouble(row.fieldIndex("open"));
        double volume = row.getDouble(row.fieldIndex("volume"));
        
        // Since the timestamp is a long, it seems like you're getting milliseconds since epoch
        long timestampMillis = row.getLong(row.fieldIndex("timestamp"));
        
        // Create a new Date object from timestamp (in milliseconds)
        Date timestamp = new Date(timestampMillis);
    
        // Return a new CryptoData object (with all expected parameters)
        return new CryptoData(ticker,open,high,low,close, volume, timestamp);
    }
    

    public static List<AverageData> runBatch(SparkSession sparkSession, String saveFile) {
        System.out.println("Running Batch Processing");

        // Reading parquet file
        var dataFrame = sparkSession.read().parquet(saveFile);
        System.out.println(dataFrame);

        // Converting DataFrame to RDD of CryptoData objects
        JavaRDD<CryptoData> rdd = dataFrame.javaRDD().map(row -> ProcessorUtils.transformData(row));

        // Extracting Open, Close, and Timestamp fields
        JavaRDD<Double> openPrices = rdd.map(CryptoData::getOpen);
        JavaRDD<Double> closePrices = rdd.map(CryptoData::getClose);

        // Converting timestamp to java.sql.Date and calculating average timestamp
        JavaRDD<Date> timestamps = rdd.map(data -> new Date(data.getTimestamp().getTime())); // Conversion to
                                                                                             // java.sql.Date
        double avgTimestamp = timestamps.mapToDouble(Date::getTime).reduce((t1, t2) -> t1 + t2) / timestamps.count();

        // Calculating averages for Open and Close Prices
        double avgOpen = openPrices.reduce((value1, value2) -> value1 + value2) / openPrices.count();
        double avgClose = closePrices.reduce((value1, value2) -> value1 + value2) / closePrices.count();

        // Printing out the calculated averages
        System.out.println("Avg Open Price : " + avgOpen);
        System.out.println("Avg Close Price : " + avgClose);
        System.out.println("Avg Timestamp : " + avgTimestamp);

        // Creating AverageData object with timestamp as double
        AverageData avgData = new AverageData("BTC", avgTimestamp, avgOpen, avgClose);

        // Returning the list of AverageData
        List<AverageData> averageDataList = new ArrayList<>();
        averageDataList.add(avgData);

        return averageDataList;
    }

    // Save AverageData to Cassandra
    public static void saveAvgToCassandra(JavaRDD<AverageData> rdd) {

        CassandraJavaUtil.javaFunctions(rdd)
                .writerBuilder("cryptodatakeyspace", "average_data", CassandraJavaUtil.mapToRow(AverageData.class))
                .saveToCassandra();

        if (rdd.isEmpty()) {
            System.out.println("RDD is empty. No data to save.");
        } else {
            System.out.println("RDD contains " + rdd.count() + " records.");
        }

        // Print a huge line with the message
        System.out.println(
                "================================================ SAVED  AVG TO CASSANDRA =================================================");
    }

}

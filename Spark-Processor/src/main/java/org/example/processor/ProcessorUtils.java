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
        columnNameMappings.put("open", "open");
        columnNameMappings.put("close", "close");
        columnNameMappings.put("high", "high");
        columnNameMappings.put("low", "low");
        columnNameMappings.put("volume", "volume");
        columnNameMappings.put("timestamp", "timestamp");

        // Save data to Cassandra
        javaFunctions(dataStream).writerBuilder("CryptoDatakeyspace", "cryptodata",
                CassandraJavaUtil.mapToRow(CryptoData.class, columnNameMappings)).saveToCassandra();
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
        return new CryptoData(row.getString(0), row.getDouble(1), row.getDouble(2), row.getDouble(3), row.getDouble(4),
                row.getDouble(5), 0, new Date(row.getLong(6)));
    }

    // Run batch processing to calculate average values (e.g., average open/close
    // price)
    public static List<AverageData> runBatch(SparkSession sparkSession, String saveFile) {
        System.out.println("Running Batch Processing");

        var dataFrame = sparkSession.read().parquet(saveFile);
        System.out.println(dataFrame);
        JavaRDD<CryptoData> rdd = dataFrame.javaRDD().map(row -> ProcessorUtils.transformData(row));

        JavaRDD<Double> openPrices = rdd.map(CryptoData::getOpen);
        JavaRDD<Double> closePrices = rdd.map(CryptoData::getClose);

        double avgOpen = openPrices.reduce((value1, value2) -> value1 + value2) / openPrices.count();
        double avgClose = closePrices.reduce((value1, value2) -> value1 + value2) / closePrices.count();

        System.out.println("Avg Open Price : " + avgOpen);
        System.out.println("Avg Close Price : " + avgClose);

        AverageData avgData = new AverageData("0", avgOpen, avgClose);
        List<AverageData> averageDataList = new ArrayList<>();
        averageDataList.add(avgData);

        return averageDataList;
    }

    // Save AverageData to Cassandra
    public static void saveAvgToCassandra(JavaRDD<AverageData> rdd) {
        CassandraJavaUtil.javaFunctions(rdd)
                .writerBuilder("CryptoDatakeyspace", "averagedata", CassandraJavaUtil.mapToRow(AverageData.class))
                .saveToCassandra();
    }

}

# Spark Streaming: Real-Time Processing

This project features data processing using Spark Streaming, Kafka, and Cassandra. It simulates an IoT data pipeline with batch processing capabilities in a Lambda architecture.

---

## Features

- **Data Ingestion**: Simulates IoT sensor data streams in JSON format.
- **Streaming with Kafka**: Streams data to Apache Kafka topics using a Java-based Kafka Producer.
- **Speed Layer**: Processes real-time data using Spark Streaming and Kafka.
- **Batch Layer**: Implements batch processing with HDFS, Spark, Hive, and Sqoop.
- **Data Persistence**: Stores processed data in Cassandra for low-latency access.
- **Real-Time Analytics**: Enables monitoring and analysis of data streams through custom dashboards and tools.

---

## Quick Start

### 1. Clone the Repository
```bash
git clone https://github.com/samiimasmoudii/BigData-Speed-Processing-with-Spark-Streaming.git

cd 
```

### 2. Start the Environment
```bash
docker-compose up -d
```

### 3. Run the Producers 
```bash
mvn clean package
//choose a name for the jar you're going to generate
```
```bash 
//run these commands
docker exec namenode hdfs dfs -rm -r /lambda-arch
docker exec namenode hdfs dfs -mkdir -p /lambda-arch
docker exec namenode hdfs dfs -mkdir -p /lambda-arch/checkpoint
docker exec namenode hdfs dfs -chmod -R 777 /lambda-arch
docker exec namenode hdfs dfs -chown -R 777 /lambda-arch
docker exec namenode hdfs dfs -chmod -R 777 /lambda-arch/checkpoint
docker exec namenode hdfs dfs -chown -R 777 /lambda-arch/checkpoint
```


Create Schemas in Cassandra 
```bash 
docker exec cassandra-iot cqlsh --username cassandra --password cassandra -f /schema.cql
```
Copy and execeute the kafka producer 

```bash
docker cp kafka-producer-1.0.0.jar kafka-iot:/
docker exec -it kafka-iot java -jar kafka-producer-1.0.0.jar
```

Copy And execute the spark producer
```bash
docker cp spark-processor-1.0.0.jar spark-master:/
docker exec spark-master /spark/bin/spark-submit --class org.example.processor.StreamProcessor /spark-processor-1.0.0.jar
```







### 4. Monitor Data Streams
```bash
docker exec -it cassandra-iot cqlsh -u cassandra -p cassandra
DESCRIBE KEYSPACES;
SELECT * FROM sensordatakeyspace.temperature;
SELECT * FROM sensordatakeyspace.humidity;
---

## Technologies Used

- **Java & Maven**: For the Kafka Producer and project build.
- **Apache Kafka**: As the core streaming platform.
- **Apache Spark**: For both streaming and batch data processing.
- **Apache Cassandra**: For data persistence.
- **HDFS, Hive, and Sqoop**: For batch layer processing.
- **Docker**: To containerize the environment for deployment.
- **Jackson**: For JSON serialization.

---

## Author

**Sami MASMOUDI**  
Software Engineering Student  
[LinkedIn](https://linkedin.com/in/sami-masmoudi12) | [GitHub](https://github.com/samiimasmoudii)

---

Feel free to explore, contribute, or raise issues in this repository!


# Real-Time Cryptocurrency Data Processing

This project demonstrates real-time cryptocurrency data processing using Apache Kafka, Spark Streaming, and Cassandra. It includes ingesting, processing, and persisting crypto data for real-time analytics, with support for batch computations.

---

## Features

- **Data Ingestion**: Fetches real-time cryptocurrency data from [Polygon.io](https://polygon.io/) using their API (requires a free API key).
- **Streaming with Kafka**: Streams the ingested data to Apache Kafka topics using a Java-based Kafka Producer.
- **Real-Time Processing**: Processes the data in real-time with Spark Streaming, calculating various metrics.
- **Batch Processing**: Computes averages and aggregates using a batch processing approach.
- **Data Persistence**: Saves processed data into Cassandra for low-latency access.
- **Serving Layer**: Displays cryptocurrency data and metrics in a custom dashboard.

---

## Project Structure

- **kafka-producer**: Contains all Java files for ingesting and encoding real-time data from the Polygon.io API into Kafka.
- **spark-processor**:
  - **StreamProcessor**: Handles real-time data processing and sends processed data to Cassandra.
  - **BatchProcessor**: Performs batch computations, such as averages, and saves the results in Cassandra.
- **data**: Contains Cassandra schemas required for this project. Copy these schemas to the Cassandra container during setup.
- **dashboard**: Provides a visualization layer for real-time and batch-processed crypto data.

---

## Quick Start

### 1. Clone the Repository
```bash
git clone https://github.com/samiimasmoudii/BigData-Speed-Processing-with-Spark-Streaming.git
cd BigData-Speed-Processing-with-Spark-Streaming
```

### 2. Start the Environment
Ensure Docker is installed and running. Use the following command to set up the environment:
```bash
docker-compose up -d
```

### 3. Prepare HDFS (if needed for batch processing)
Run these commands to set up directories in HDFS:
```bash
docker exec namenode hdfs dfs -rm -r /lambda-arch
docker exec namenode hdfs dfs -mkdir -p /lambda-arch/checkpoint
docker exec namenode hdfs dfs -chmod -R 777 /lambda-arch
docker exec namenode hdfs dfs -chown -R 777 /lambda-arch
```

### 4. Create Cassandra Schemas
Load the Cassandra schemas into the database:
the Schemas file is located under data/schemas.cql
```bash
docker exec cassandra-iot cqlsh --username cassandra --password cassandra -f /schema.cql
```

### 5. Run the Kafka Producer
Build the Kafka producer jar file:
```bash
mvn clean package
```
Copy and execute the Kafka producer:
```bash
docker cp kafka-producer-1.0.0.jar kafka-iot:/
docker exec -it kafka-iot java -jar kafka-producer-1.0.0.jar
```

### 6. Run the Spark Processor
Build the Spark processor jar file and execute it:
```bash
docker cp spark-processor-1.0.0.jar spark-master:/
docker exec spark-master /spark/bin/spark-submit --class org.example.processor.StreamProcessor /spark-processor-1.0.0.jar
```

---

## Monitor Data Streams

Connect to Cassandra to verify the stored data:
```bash
docker exec -it cassandra-iot cqlsh -u cassandra -p cassandra
```
Run the following queries to inspect the data:
```sql
DESCRIBE KEYSPACES;
SELECT * FROM cryptodatakeyspace.crypto_data;
SELECT * FROM cryptodatakeyspace.aggregated_data;
SELECT * FROM cryptodatakeyspace.average_data;
```

---

## Technologies Used

- **Java & Maven**: For building Kafka and Spark processors.
- **Apache Kafka**: Used as the core streaming platform.
- **Apache Spark**: For both real-time and batch data processing.
- **Apache Cassandra**: Serves as the database for processed data.
- **Docker**: Containerizes the environment for easy deployment.
- **Jackson**: For JSON serialization.

---

## Author

**Sami MASMOUDI**  
Software Engineering Student  
[GitHub](https://github.com/samiimasmoudii)


**Amine Ziadi**  
Software Engineering Student  
[GitHub](https://github.com/aminewho)

---

Feel free to explore, contribute, or raise issues in this repository!


package com.bigdata.dashboard.repository;

import com.bigdata.dashboard.entity.CryptoData;

import java.util.List;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CryptoDataRepository extends CassandraRepository<CryptoData, String> {

	// Fetch all crypto data
	@Query("SELECT * FROM cryptodatakeyspace.crypto_data")
	Iterable<CryptoData> find();

    // Fetch crypto data for a specific ticker
    @Query("SELECT * FROM cryptodatakeyspace.crypto_data WHERE ticker = ?0 ALLOW FILTERING")
    Iterable<CryptoData> findByTicker(String ticker);

    // Fetch crypto data within a specific time range
    @Query("SELECT * FROM cryptodatakeyspace.crypto_data WHERE ticker = ?0 AND timestamp >= ?1 AND timestamp <= ?2 ALLOW FILTERING")
    Iterable<CryptoData> findByTickerAndTimestamp(String ticker, String startTime, String endTime);
}

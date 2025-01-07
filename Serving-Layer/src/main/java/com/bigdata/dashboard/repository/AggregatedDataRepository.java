package com.bigdata.dashboard.repository;

import com.bigdata.dashboard.entity.AggregatedData;
import com.bigdata.dashboard.entity.AverageData;

import java.util.Date;
import java.util.List;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AggregatedDataRepository extends CassandraRepository<AggregatedData, String> {
    List<AverageData> findByTimestampAfter(Date timestamp);

    // Fetch all aggregated data
    @Query("SELECT * FROM cryptodatakeyspace.aggregated_data")
    List<AggregatedData> findAll();

    // Fetch aggregated data for a specific ticker
    @Query("SELECT * FROM cryptodatakeyspace.aggregated_data WHERE ticker = ?0 ALLOW FILTERING")
    Iterable<AggregatedData> findByTicker(String ticker);

    // Fetch aggregated data for a specific period range
    @Query("SELECT * FROM cryptodatakeyspace.aggregated_data WHERE ticker = ?0 AND period_start >= ?1 AND period_start <= ?2 ALLOW FILTERING")
    Iterable<AggregatedData> findByTickerAndPeriod(String ticker, String startPeriod, String endPeriod);
}

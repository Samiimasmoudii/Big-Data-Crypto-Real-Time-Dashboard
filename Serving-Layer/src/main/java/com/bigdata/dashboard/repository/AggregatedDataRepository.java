package com.bigdata.dashboard.repository;

import com.bigdata.dashboard.entity.AggregatedData;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AggregatedDataRepository extends CassandraRepository<AggregatedData, String> {

    // Query to fetch all aggregated data entries
    @Query("SELECT * FROM cryptodatakeyspace.aggregated_data")
    Iterable<AggregatedData> findAllAggregatedData();

    // Query to fetch aggregated data for a specific ticker
    @Query("SELECT * FROM cryptodatakeyspace.aggregated_data WHERE ticker = ?0 ALLOW FILTERING")
    Iterable<AggregatedData> findByTicker(String ticker);

    // Query to fetch aggregated data for a specific period range (e.g., for a day/week/month)
    @Query("SELECT * FROM cryptodatakeyspace.aggregated_data WHERE ticker = ?0 AND period_start >= ?1 AND period_start <= ?2 ALLOW FILTERING")
    Iterable<AggregatedData> findByTickerAndPeriod(String ticker, String startPeriod, String endPeriod);
}

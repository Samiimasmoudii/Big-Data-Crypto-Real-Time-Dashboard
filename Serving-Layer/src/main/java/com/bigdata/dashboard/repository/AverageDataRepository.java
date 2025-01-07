package com.bigdata.dashboard.repository;

import com.bigdata.dashboard.entity.AverageData;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AverageDataRepository extends CassandraRepository<AverageData, String> {

    // Fetch all average data entries
    @Query("SELECT * FROM cryptodatakeyspace.average_data")
    Iterable<AverageData> find();

    // Fetch average data for a specific ticker
    @Query("SELECT * FROM cryptodatakeyspace.average_data WHERE ticker = ?0 ALLOW FILTERING")
    Iterable<AverageData> findByTicker(String ticker);
}

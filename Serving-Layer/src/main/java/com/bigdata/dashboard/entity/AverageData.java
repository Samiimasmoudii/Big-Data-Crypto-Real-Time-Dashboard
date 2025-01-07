package com.bigdata.dashboard.entity;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.io.Serializable;

@Table("average_data")
public class AverageData implements Serializable {

    @PrimaryKeyColumn(name = "ticker", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String ticker;

    @Column("average_price")
    private double averagePrice;

    @Column("average_volume")
    private double averageVolume;

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public double getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(double averagePrice) {
        this.averagePrice = averagePrice;
    }

    public double getAverageVolume() {
        return averageVolume;
    }

    public void setAverageVolume(double averageVolume) {
        this.averageVolume = averageVolume;
    }

    @Override
    public String toString() {
        return "AverageData [ticker=" + ticker + ", averagePrice=" + averagePrice + ", averageVolume=" + averageVolume + "]";
    }
}

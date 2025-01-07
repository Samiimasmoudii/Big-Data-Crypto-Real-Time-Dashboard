package com.bigdata.dashboard.entity;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.io.Serializable;
import java.util.Date;

@Table("aggregated_data")
public class AggregatedData implements Serializable {

    @PrimaryKeyColumn(name = "ticker", ordinal = 0)
    private String ticker;

    @PrimaryKeyColumn(name = "period_start", ordinal = 1)
    private Date periodStart;

    @Column("average_price")
    private double averagePrice;

    @Column("total_volume")
    private double totalVolume;

    // Getters and Setters
    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public Date getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(Date periodStart) {
        this.periodStart = periodStart;
    }

    public double getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(double averagePrice) {
        this.averagePrice = averagePrice;
    }

    public double getTotalVolume() {
        return totalVolume;
    }

    public void setTotalVolume(double totalVolume) {
        this.totalVolume = totalVolume;
    }

    @Override
    public String toString() {
        return "AggregatedData [ticker=" + ticker + ", periodStart=" + periodStart + ", averagePrice=" + averagePrice + ", totalVolume=" + totalVolume + "]";
    }
}

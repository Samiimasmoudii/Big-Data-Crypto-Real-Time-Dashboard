package com.bigdata.dashboard.entity;

import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.io.Serializable;
import java.util.Date;

@Table("crypto_data")
public class CryptoData implements Serializable {

    @PrimaryKeyColumn(name = "ticker", ordinal = 0, type = PrimaryKeyType.PARTITIONED)
    private String ticker;

    @PrimaryKeyColumn(name = "timestamp", ordinal = 1)
    private Date timestamp;

    @Column("price")
    private double price;

    @Column("volume")
    private double volume;

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    @Override
    public String toString() {
        return "CryptoData [ticker=" + ticker + ", timestamp=" + timestamp + ", price=" + price + ", volume=" + volume + "]";
    }
}

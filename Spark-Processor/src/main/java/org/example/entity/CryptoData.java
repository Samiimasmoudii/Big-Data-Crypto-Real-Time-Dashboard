package org.example.entity;

import java.io.Serializable;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

public class CryptoData implements Serializable {

    private String id;
    private double open;
    private double high;
    private double low;
    private double close;
    private double volume;
    private double marketCap;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "MST")
    private Date timestamp;

    public CryptoData() {
        // Default constructor
    }

    public CryptoData(String id, double open, double high, double low, double close, double volume, double marketCap, Date timestamp) {
        this.id = id;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
        this.marketCap = marketCap;
        this.timestamp = timestamp;
    }

    // Getter methods
    public String getId() {
        return id;
    }

    public double getOpen() {
        return open;
    }

    public double getHigh() {
        return high;
    }

    public double getLow() {
        return low;
    }

    public double getClose() {
        return close;
    }

    public double getVolume() {
        return volume;
    }

    public double getMarketCap() {
        return marketCap;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    // Optionally, add setters if needed
}

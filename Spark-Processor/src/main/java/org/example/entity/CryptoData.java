package org.example.entity;

import java.io.Serializable;
import java.sql.Date;
import com.fasterxml.jackson.annotation.JsonFormat;

public class CryptoData implements Serializable {

    private String ticker;
    private double open;
    private double high;
    private double low;
    private double close;
    private double volume;
    // Optional, remove if not needed
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "MST")
    private Date timestamp;

    // Constructor
    public CryptoData(String ticker, double open, double high, double low, double close, double volume,  Date timestamp) {
        this.ticker = ticker;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
        
        this.timestamp = timestamp;
    }

    // Getter methods
    public String getTicker() { return ticker; }
    public double getOpen() { return open; }
    public double getHigh() { return high; }
    public double getLow() { return low; }
    public double getClose() { return close; }
    public double getVolume() { return volume; }
    public Date getTimestamp() { return timestamp; }
}

package org.example;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CryptoData implements Serializable {

    private String ticker;
    private double open;
    private double close;
    private double high;
    private double low;
    private double volume;
    private Date timestamp;

    public CryptoData() {
    }

    public CryptoData(String ticker, double open, double close, double high, double low, double volume, Date timestamp) {
        this.ticker = ticker;
        this.open = open;
        this.close = close;
        this.high = high;
        this.low = low;
        this.volume = volume;
        this.timestamp = timestamp;
    }

    public String getTicker() {
        return ticker;
    }

    public double getOpen() {
        return open;
    }

    public double getClose() {
        return close;
    }

    public double getHigh() {
        return high;
    }

    public double getLow() {
        return low;
    }

    public double getVolume() {
        return volume;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    // Override toString() to format the timestamp as a readable date
    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        return "CryptoData{" +
                "ticker='" + ticker + '\'' +
                ", open=" + open +
                ", close=" + close +
                ", high=" + high +
                ", low=" + low +
                ", volume=" + volume +
                ", timestamp=" + timestamp + // Formatted date here for logs
                '}';
    }
    
}

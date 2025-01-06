package org.example;

import java.io.Serializable;

public class CryptoData implements Serializable {

    private String ticker;
    private double open;
    private double close;
    private double high;
    private double low;
    private double volume;
    private long timestamp;

    public CryptoData() {
    }

    public CryptoData(String ticker, double open, double close, double high, double low, double volume, long timestamp) {
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

    public long getTimestamp() {
        return timestamp;
    }
}

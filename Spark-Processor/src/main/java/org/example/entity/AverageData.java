package org.example.entity;

import java.io.Serializable;

public class AverageData implements Serializable {

    private String ticker;
    private Double timestamps;
    private double averagePrice;
    private double avgClose;

    // Default constructor
    public AverageData() {
    }

    // Parameterized constructor
    public AverageData(String ticker, Double timestamps, double averagePrice, double avgClose) {
        this.ticker = ticker;
        this.timestamps = timestamps;
        this.averagePrice = averagePrice;
        this.avgClose = avgClose;
    }

    // Getters
    public String getTicker() {
        return ticker;
    }

    public Double getTimestamps() {
        return timestamps;
    }

    public double getAveragePrice() {
        return averagePrice;
    }

    public double getavgClose() {
        return avgClose;
    }
}

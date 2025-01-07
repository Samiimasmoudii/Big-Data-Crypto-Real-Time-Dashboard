package org.example.entity;

import java.io.Serializable;

public class AverageData implements Serializable {

    private String id;
    private double averagePrice;
    private double volume;

    public AverageData() {
    }

    public AverageData(String id, double averagePrice, double volume) {
        this.id = id;
        this.averagePrice = averagePrice;
        this.volume = volume;
    }

    public String getId() {
        return id;
    }

    public double getAveragePrice() {
        return averagePrice;
    }

    public double getVolume() {
        return volume;
    }
}
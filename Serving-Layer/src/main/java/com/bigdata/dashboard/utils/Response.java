package com.bigdata.dashboard.utils;

import java.io.Serializable;

public class Response implements Serializable {
    
    private double averagePrice;
    private double volume;

    public double getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(double averagePrice) {
        this.averagePrice = averagePrice;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    @Override
    public String toString() {
        return "Response [averagePrice=" + averagePrice + ", volume=" + volume + "]";
    }
}

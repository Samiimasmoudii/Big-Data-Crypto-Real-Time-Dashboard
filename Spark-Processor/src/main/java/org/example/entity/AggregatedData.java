
package org.example.entity;

import java.sql.Date;

public class AggregatedData {
    private String ticker;
    private Date periodStart;
    private Double avgPrice;

    // Constructor
    public AggregatedData(String ticker, java.sql.Date periodStart2, Double avgPrice) {
        this.ticker = ticker;
        this.periodStart = periodStart2;
        this.avgPrice = avgPrice;
    }

    // Getters and setters
    public String getTicker() { return ticker; }
    public void setTicker(String ticker) { this.ticker = ticker; }

    public Date getPeriodStart() { return periodStart; }
    public void setPeriodStart(Date periodStart) { this.periodStart = periodStart; }

    public Double getAvgPrice() { return avgPrice; }
    public void setAvgPrice(Double avgPrice) { this.avgPrice = avgPrice; }
}

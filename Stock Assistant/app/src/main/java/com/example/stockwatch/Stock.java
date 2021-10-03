package com.example.stockwatch;

public class Stock {
    private String symbol;
    private String companyName;
    private Double latestPrice;
    private Double change;
    private Double changePercentage;

    public Stock() {
        this.symbol = "";
        this.companyName = "";
        this.latestPrice = 0.0;
        this.change = 0.0;
        this.changePercentage = 0.0;

    }

    public Stock(String symbol, String companyName)
    {
        this.symbol = symbol;
        this.companyName = companyName;
        this.latestPrice = 0.0;
        this.change = 0.0;
        this.changePercentage = 0.0;

    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Double getLatestPrice() {
        return latestPrice;
    }

    public void setLatestPrice(Double latestPrice) {
        this.latestPrice = latestPrice;
    }

    public Double getChange() {
        return change;
    }

    public void setChange(Double change) {
        this.change = change;
    }

    public Double getChangePercentage() {
        return changePercentage;
    }

    public void setChangePercentage(Double changePercentage) {
        this.changePercentage = changePercentage;
    }
}

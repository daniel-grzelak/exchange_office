package com.daniel.exchangeoffice.classes;

public class GridCurrencyModel {
    private String name;
    private String exchangeRate;

    public GridCurrencyModel(String name, String exchangeRate) {
        this.name = name;
        this.exchangeRate = exchangeRate;
    }

    public GridCurrencyModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setExchangeRate(String exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getExchangeRate() {
        return exchangeRate;
    }
}

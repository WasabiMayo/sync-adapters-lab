package com.example.wasabi.syncadapterlab;

/**
 * Created by Wasabi on 3/2/2016.
 */
public class Stock {
    private String Name;
    private String LastPrice;

    public String getLastPrice() {
        return LastPrice;
    }

    public void setLastPrice(String lastPrice) {
        LastPrice = lastPrice;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}

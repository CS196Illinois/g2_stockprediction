package com.example.g2_stockprediction;

public class Stock {
    long id;
    private String symbol;
    private String price;
    private String key;

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    private String logo;

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    private String low;

    public long getId() {
        return id;
    }

    public void setId(long setId) {
        this.id = setId;
    }

    public String getSymbol(){
        return symbol;
    }

    public void setSymbol(String setSymbol){
        this.symbol = setSymbol;
    }

    public String getPrice(){
        return price;
    }

    public void setPrice(String setPrice){
        this.price = setPrice;
    }

    public void setKey(String setKey) {
        this.key = setKey;
    }

    public String getKey() {
        return key;
    }
}

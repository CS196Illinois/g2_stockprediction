package com.example.apipractice;

public class Stock {
    long id;
    private String symbol;
    private String price;

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
}

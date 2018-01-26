package com.shop.atominac.shopend;


public class UpdateModel {
    private String item_name;
    private String price;
    private String  brand;

    public String getItem_name() {
        return item_name;
    }

    public String getPrice() {
        return price;
    }

    public String getBrand() {
        return brand;
    }



    public UpdateModel(String item_name, String price, String brand) {
        this.item_name = item_name;
        this.price = price;
        this.brand = brand;
    }
}

package com.shop.atominac.shopend;


class Delivery_boy_model {
    private String name;
    private String status;
    private String location;
    private String token;
    private  String boyId ;


    Delivery_boy_model(String boyId, String name, String status, String location, String token) {
        this.name = name;
        this.status = status;
        this.location = location;
        this.token = token;
        this.boyId = boyId ;
    }

    public String getName() {
        return name;
    }

    public String getBoyId() {
        return boyId;
    }

    public String getStatus() {
        return status;
    }

    public String getLocation() {
        return location;
    }

    public String getToken() {
        return token;
    }
}

package com.shop.atominac.shopend;


public class Autobuy_model {





    private String name;
    private String status;
    private String location;
    private String Recievedtoken;


    public Autobuy_model(String name, String status, String location, String token) {
        this.name = name;
        this.status = status;
        this.location = location;
        this.Recievedtoken = token;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getLocation() {
        return location;
    }

    public String getToken() {
        return Recievedtoken;
    }
}

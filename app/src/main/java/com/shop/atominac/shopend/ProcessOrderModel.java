package com.shop.atominac.shopend;


public class ProcessOrderModel {


    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getAmount() {
        return amount;
    }

    public String getPayment() {
        return payment;
    }

    public String getId() {
        return id;
    }

    public String getList() {
        return list;
    }
    public String getTime() {
        return time;
    }


    private String address;
    private String amount;
    private String payment;
    private String list ;
    private String name;
    private String time;
    private String status;
    private String id;
    public String getStatus() {
        return status;
    }

    public ProcessOrderModel(String name, String address, String amount, String payment, String list, String time, String status, String id) {
        this.name = name;
        this.address = address;
        this.amount = amount;
        this.payment = payment;
        this.list = list;
        this.time=time;
        this.status=status;
        this.id=id;
    }
}

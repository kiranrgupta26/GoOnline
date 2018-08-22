package com.example.kiran.vs;

/**
 * Created by Kiran on 2/20/2018.
 */

public class MyOrderPOJO {


    private String orderid;
    private String name;
    private String price;
    private String quantity;


    public MyOrderPOJO(String orderid,String name)
    {
        this.orderid = orderid;
        this.name = name;
        //this.price = price;
       // this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getQuantity() {
        return quantity;
    }
    public String getOrderid() {
        return orderid;
    }



}

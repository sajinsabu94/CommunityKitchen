package com.xeta.communitykitchen;

import java.io.Serializable;

/**
 * Created by apple on 26-03-2020.
 */

public class Order{
    public String name;
    public String age;
    public String landmark;
    public String ward;
    public String order_date;
    public String address;
    public String mobile;
    public String qty;
    public String order_by;

    public Order(){

    }

    public Order(String name, String age, String landmark, String ward, String order_date, String address, String mobile, String qty, String order_by) {
        this.name = name;
        this.age = age;
        this.landmark = landmark;
        this.ward = ward;
        this.order_date = order_date;
        this.address = address;
        this.mobile = mobile;
        this.qty = qty;
        this.order_by = order_by;
    }
}

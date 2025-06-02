package com.example.cafe_harmony;

import java.io.Serializable;
import java.util.List;

public class Order implements Serializable {
    public List<CartItem> items;
    public int total;
    public String paymentMethod;
    public String timestamp;

    public Order() {} // Required for Firebase

    public Order(List<CartItem> items, int total, String paymentMethod, String timestamp) {
        this.items = items;
        this.total = total;
        this.paymentMethod = paymentMethod;
        this.timestamp = timestamp;
    }
}

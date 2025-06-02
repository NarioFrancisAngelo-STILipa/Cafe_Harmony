package com.example.cafe_harmony;

import java.io.Serializable;

public class CartItem implements Serializable {
    public String name;
    public int quantity;
    public int price;

    public CartItem(String name, int quantity, int price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }
}

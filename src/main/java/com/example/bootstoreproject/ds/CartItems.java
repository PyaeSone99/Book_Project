package com.example.bootstoreproject.ds;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.LinkedList;
import java.util.Objects;

//id,title,price,quantity
@Getter
@Setter
@ToString
public class CartItems {
    private int id;
    private String title;
    private double price;
    private int quantity;
    private boolean render;
    private LinkedList<Integer> quanLinkedList = new LinkedList<>();

    public CartItems(){

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItems cartItems = (CartItems) o;
        return id == cartItems.id && title.equals(cartItems.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }

    public CartItems(int id, String title, double price, int quantity) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.quantity = quantity;
    }
}








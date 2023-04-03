package com.example.bootstoreproject.ds;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.util.HashSet;
import java.util.Set;

@SessionScope
@Component
public class Cart {
    private Set<CartItems> cartItems = new HashSet<>();

    public Set<CartItems>getCartItems(){
        return cartItems;
    }
    public void addToCart(CartItems cartItem){
        this.cartItems.add(cartItem);
    }
    public void removeFromCart(CartItems cartItem){
        this.cartItems.remove(cartItem);
    }

    public void clearCart(){
        this.cartItems.clear();
    }

    public int cartSize(){
        return this.cartItems.size();
    }

    public void setCartItems(Set<CartItems> cartItems) {
        this.cartItems = cartItems;
    }
}

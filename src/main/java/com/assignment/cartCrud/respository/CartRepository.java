package com.assignment.cartCrud.respository;

import com.assignment.cartCrud.model.Cart;

import java.util.Iterator;
import java.util.Optional;

public interface CartRepository {
    void addCart(Cart cart);
    Optional<Cart> getCart(String id);
    void updateCart(Cart cart);
    void deleteCart(String id);
    Iterator<Cart> getAllCarts();
}

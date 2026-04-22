package com.assignment.cartCrud.respository;

import com.assignment.cartCrud.model.Cart;
import com.assignment.cartCrud.model.Product;

import java.util.Iterator;
import java.util.Optional;

public interface CartRepository {
    void addCart(Cart cart);
    Optional<Cart> getCart(String id);
    void updateCart(Cart cart);
    boolean deleteCart(String id);
    boolean addProductToCart(String cartId, Product product);
    Iterator<Cart> getAllCarts();
}

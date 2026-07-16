package com.assignment.cartCrud.respository;

import com.assignment.cartCrud.model.Cart;
import com.assignment.cartCrud.model.Product;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Optional;

public interface CartRepository {
    void addCart(Cart cart);
    Optional<Cart> getCart(String id);
    Optional<Cart> getAndTouchCart(String id);
    boolean deleteCart(String id);
    boolean addProductToCart(String cartId, Product product);
    int deleteExpiredCarts(LocalDateTime expirationThreshold);
    Iterator<Cart> getAllCarts();
}

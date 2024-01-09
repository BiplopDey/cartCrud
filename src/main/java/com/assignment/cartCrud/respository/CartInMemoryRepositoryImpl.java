package com.assignment.cartCrud.respository;

import com.assignment.cartCrud.model.Cart;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class CartInMemoryRepositoryImpl implements CartRepository{
    private final Map<String, Cart> carts = new ConcurrentHashMap<>();

    @Override
    public void addCart(Cart cart) {
        carts.put(cart.getId(), cart);
    }

    @Override
    public Optional<Cart> getCart(String id) {
        return Optional.ofNullable(carts.get(id));
    }

    @Override
    public void updateCart(Cart cart) {
        carts.put(cart.getId(), cart);
    }

    @Override
    public void deleteCart(String id) {
        carts.remove(id);
    }
}

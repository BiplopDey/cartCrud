package com.assignment.cartCrud.respository;

import com.assignment.cartCrud.model.Cart;
import com.assignment.cartCrud.model.Product;
import org.springframework.stereotype.Repository;

import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

@Repository
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
    public boolean deleteCart(String id) {
        return carts.remove(id) != null;
    }

    @Override
    public boolean addProductToCart(String cartId, Product product) {
        AtomicBoolean added = new AtomicBoolean(false);
        carts.computeIfPresent(cartId, (id, cart) -> {
            cart.getProducts().add(product);
            cart.updateLastAccessedTime();
            added.set(true);
            return cart;
        });
        return added.get();
    }

    @Override
    public Iterator<Cart> getAllCarts() {
        return carts.values().iterator();
    }
}

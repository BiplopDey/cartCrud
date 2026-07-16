package com.assignment.cartCrud.respository;

import com.assignment.cartCrud.model.Cart;
import com.assignment.cartCrud.model.Product;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Repository
@Profile("!sqlite")
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
    public Optional<Cart> getAndTouchCart(String id) {
        AtomicReference<Cart> touchedCart = new AtomicReference<>();
        carts.computeIfPresent(id, (cartId, cart) -> {
            cart.updateLastAccessedTime();
            touchedCart.set(cart);
            return cart;
        });
        return Optional.ofNullable(touchedCart.get());
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
    public int deleteExpiredCarts(LocalDateTime expirationThreshold) {
        AtomicInteger deleted = new AtomicInteger();
        carts.forEach((id, cart) -> carts.computeIfPresent(id, (cartId, currentCart) -> {
            if (!currentCart.getLastAccessedTime().isAfter(expirationThreshold)) {
                deleted.incrementAndGet();
                return null;
            }
            return currentCart;
        }));
        return deleted.get();
    }

    @Override
    public Iterator<Cart> getAllCarts() {
        return carts.values().iterator();
    }
}

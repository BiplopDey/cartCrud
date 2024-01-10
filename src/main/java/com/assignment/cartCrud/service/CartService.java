package com.assignment.cartCrud.service;

import com.assignment.cartCrud.model.Cart;
import com.assignment.cartCrud.model.Product;
import com.assignment.cartCrud.respository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;

    public Cart createCart() {
        Cart cart = new Cart();
        cart.setId(UUID.randomUUID().toString());
        cart.setCreationTime(LocalDateTime.now());
        cartRepository.addCart(cart);
        return cart;
    }

    public Optional<Cart> getCart(String id) {
        Optional<Cart> cartOptional = cartRepository.getCart(id);
        cartOptional.ifPresent(this::update);
        return cartOptional;
    }


    public void addProductToCart(String cartId, Product product) {
        cartRepository.getCart(cartId).ifPresent(cart -> {
            cart.getProducts().add(product);
            update(cart);
        });
    }

    public void deleteCart(String id) {
        cartRepository.deleteCart(id);
    }

    private void update(Cart cart) {
        cart.updateLastAccessedTime();
        cartRepository.updateCart(cart);
    }
}

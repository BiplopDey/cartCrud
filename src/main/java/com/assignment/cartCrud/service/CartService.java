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
        cartOptional.ifPresent(this::touchCart);
        return cartOptional;
    }

    public boolean addProductToCart(String cartId, Product product) {
        return cartRepository.addProductToCart(cartId, product);
    }

    public boolean deleteCart(String id) {
        return cartRepository.deleteCart(id);
    }

    private void touchCart(Cart cart) {
        cart.updateLastAccessedTime();
        cartRepository.updateCart(cart);
    }
}

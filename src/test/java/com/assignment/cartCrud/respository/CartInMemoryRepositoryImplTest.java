package com.assignment.cartCrud.respository;

import com.assignment.cartCrud.model.Cart;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CartInMemoryRepositoryImplTest {
    private CartInMemoryRepositoryImpl cartRepository;
    private Cart testCart;

    @BeforeEach
    void setUp() {
        cartRepository = new CartInMemoryRepositoryImpl();
        testCart = new Cart();
        testCart.setId(UUID.randomUUID().toString());
    }

    @Test
    void addAndGetCart() {
        cartRepository.addCart(testCart);
        Optional<Cart> retrievedCart = cartRepository.getCart(testCart.getId());

        assertTrue(retrievedCart.isPresent());
        assertEquals(testCart.getId(), retrievedCart.get().getId());
    }

    @Test
    void updateCart() {
        cartRepository.addCart(testCart);
        testCart.setCreationTime(LocalDateTime.now()); // Assuming there's a setter for creationTime
        cartRepository.updateCart(testCart);

        Cart updatedCart = cartRepository.getCart(testCart.getId()).orElseThrow();
        assertEquals(testCart.getCreationTime(), updatedCart.getCreationTime());
    }

    @Test
    void deleteCart() {
        cartRepository.addCart(testCart);
        cartRepository.deleteCart(testCart.getId());

        assertFalse(cartRepository.getCart(testCart.getId()).isPresent());
    }

    @Test
    void getAllCarts() {
        cartRepository.addCart(testCart);
        Iterator<Cart> cartIterator = cartRepository.getAllCarts();

        assertTrue(cartIterator.hasNext());
        assertEquals(testCart.getId(), cartIterator.next().getId());
    }
}
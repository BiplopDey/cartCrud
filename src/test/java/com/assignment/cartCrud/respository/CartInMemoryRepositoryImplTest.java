package com.assignment.cartCrud.respository;

import com.assignment.cartCrud.model.Cart;
import com.assignment.cartCrud.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
    void getAndTouchCart() {
        cartRepository.addCart(testCart);
        LocalDateTime previousAccessTime = LocalDateTime.now().minusMinutes(1);
        testCart.setLastAccessedTime(previousAccessTime);

        Cart retrievedCart = cartRepository.getAndTouchCart(testCart.getId()).orElseThrow();

        assertSame(testCart, retrievedCart);
        assertTrue(retrievedCart.getLastAccessedTime().isAfter(previousAccessTime));
        assertTrue(cartRepository.getAndTouchCart("missing").isEmpty());
    }

    @Test
    void deleteCart() {
        cartRepository.addCart(testCart);

        assertTrue(cartRepository.deleteCart(testCart.getId()));
        assertFalse(cartRepository.deleteCart(testCart.getId()));
        assertFalse(cartRepository.getCart(testCart.getId()).isPresent());
    }

    @Test
    void addProductToCart() {
        cartRepository.addCart(testCart);
        Product product = new Product(1L, "Product", 10.0);

        assertTrue(cartRepository.addProductToCart(testCart.getId(), product));
        assertEquals(product, testCart.getProducts().get(0));
        assertFalse(cartRepository.addProductToCart("missing", product));
    }

    @Test
    void addProductToCartConcurrently() throws Exception {
        cartRepository.addCart(testCart);
        int requestCount = 100;
        ExecutorService executor = Executors.newFixedThreadPool(8);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch finished = new CountDownLatch(requestCount);

        try {
            for (int i = 0; i < requestCount; i++) {
                long productId = i + 1L;
                executor.submit(() -> {
                    try {
                        start.await();
                        cartRepository.addProductToCart(
                                testCart.getId(), new Product(productId, "Product", 10.0));
                    } catch (InterruptedException exception) {
                        Thread.currentThread().interrupt();
                    } finally {
                        finished.countDown();
                    }
                });
            }

            start.countDown();
            assertTrue(finished.await(5, TimeUnit.SECONDS));
            assertEquals(requestCount, testCart.getProducts().size());
        } finally {
            executor.shutdownNow();
        }
    }

    @Test
    void deleteExpiredCarts() {
        Cart activeCart = new Cart();
        activeCart.setId(UUID.randomUUID().toString());
        testCart.setLastAccessedTime(LocalDateTime.now().minusMinutes(5));
        activeCart.setLastAccessedTime(LocalDateTime.now());
        cartRepository.addCart(testCart);
        cartRepository.addCart(activeCart);

        int deleted = cartRepository.deleteExpiredCarts(LocalDateTime.now().minusMinutes(1));

        assertEquals(1, deleted);
        assertTrue(cartRepository.getCart(testCart.getId()).isEmpty());
        assertTrue(cartRepository.getCart(activeCart.getId()).isPresent());
    }

    @Test
    void getAllCarts() {
        cartRepository.addCart(testCart);
        Iterator<Cart> cartIterator = cartRepository.getAllCarts();

        assertTrue(cartIterator.hasNext());
        assertEquals(testCart.getId(), cartIterator.next().getId());
    }
}

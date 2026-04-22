package com.assignment.cartCrud.service;

import com.assignment.cartCrud.model.Cart;
import com.assignment.cartCrud.model.Product;
import com.assignment.cartCrud.respository.CartRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {
    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartService cartService;

    @Test
    public void testAddProductToCart_CartExists() {
        String cartId = UUID.randomUUID().toString();
        Product product = new Product(1L, "Some description", 10D);

        when(cartRepository.addProductToCart(cartId, product)).thenReturn(true);

        boolean result = cartService.addProductToCart(cartId, product);

        assertTrue(result);
        verify(cartRepository).addProductToCart(cartId, product);
    }

    @Test
    public void testAddProductToCart_CartDoesNotExist() {
        String cartId = UUID.randomUUID().toString();
        Product product = new Product(1L, "Some description", 10D);

        when(cartRepository.addProductToCart(cartId, product)).thenReturn(false);

        boolean result = cartService.addProductToCart(cartId, product);

        assertFalse(result);
    }

    @Test
    public void testDeleteCart_CartExists() {
        String cartId = UUID.randomUUID().toString();

        when(cartRepository.deleteCart(cartId)).thenReturn(true);

        boolean result = cartService.deleteCart(cartId);

        assertTrue(result);
        verify(cartRepository).deleteCart(cartId);
    }

    @Test
    public void testDeleteCart_CartDoesNotExist() {
        String cartId = UUID.randomUUID().toString();

        when(cartRepository.deleteCart(cartId)).thenReturn(false);

        boolean result = cartService.deleteCart(cartId);

        assertFalse(result);
    }

    @Test
    public void testGetCart_CartExists() {
        String cartId = UUID.randomUUID().toString();
        Cart mockCart = new Cart();
        mockCart.setId(cartId);

        when(cartRepository.getCart(cartId)).thenReturn(Optional.of(mockCart));

        Optional<Cart> result = cartService.getCart(cartId);

        assertTrue(result.isPresent());
        assertEquals(cartId, result.get().getId());
        verify(cartRepository).updateCart(mockCart);
    }

    @Test
    public void testGetCart_CartDoesNotExist() {
        String cartId = UUID.randomUUID().toString();

        when(cartRepository.getCart(cartId)).thenReturn(Optional.empty());

        Optional<Cart> result = cartService.getCart(cartId);

        assertFalse(result.isPresent());
    }

}

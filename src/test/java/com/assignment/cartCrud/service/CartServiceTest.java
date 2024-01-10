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
    public void testAddProductToCart() {
        String cartId = UUID.randomUUID().toString();
        Cart mockCart = new Cart();
        Product product = new Product(1L, "Some description", 10D); // Assuming Product has a no-arg constructor

        when(cartRepository.getCart(cartId)).thenReturn(Optional.of(mockCart));

        cartService.addProductToCart(cartId, product);

        assertTrue(mockCart.getProducts().contains(product));
        verify(cartRepository).updateCart(mockCart);
    }

}
package com.assignment.cartCrud.controller;

import com.assignment.cartCrud.model.Cart;
import com.assignment.cartCrud.model.Product;
import com.assignment.cartCrud.service.CartService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartController.class)
class CartControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;

    @Test
    public void createCartTest() throws Exception {
        Cart cart = new Cart();
        cart.setId("1");
        given(cartService.createCart()).willReturn(cart);

        mockMvc.perform(post("/cart")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"));
    }

    @Test
    public void getCartTest_CartExists() throws Exception {
        Cart cart = new Cart();
        cart.setId("1");
        given(cartService.getCart("1")).willReturn(Optional.of(cart));

        mockMvc.perform(get("/cart/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"));
    }

    @Test
    public void getCartTest_CartDoesNotExist() throws Exception {
        given(cartService.getCart("2")).willReturn(Optional.empty());

        mockMvc.perform(get("/cart/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void addProductToCartTest_CartExists() throws Exception {
        String productJson = "{\"id\":1,\"description\":\"Sample Product\",\"amount\":10.0}";

        given(cartService.addProductToCart(anyString(), any(Product.class))).willReturn(true);

        mockMvc.perform(post("/cart/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson))
                .andExpect(status().isOk());
    }

    @Test
    public void addProductToCartTest_CartDoesNotExist() throws Exception {
        String productJson = "{\"id\":1,\"description\":\"Sample Product\",\"amount\":10.0}";

        given(cartService.addProductToCart(anyString(), any(Product.class))).willReturn(false);

        mockMvc.perform(post("/cart/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson))
                .andExpect(status().isNotFound());
    }

    @Test
    public void addProductToCartTest_NullBody() throws Exception {
        mockMvc.perform(post("/cart/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addProductToCartTest_InvalidProduct() throws Exception {
        String invalidProductJson = "{\"description\":\"No id or amount\"}";

        mockMvc.perform(post("/cart/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidProductJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addProductToCartTest_NegativeAmount() throws Exception {
        String productJson = "{\"id\":1,\"description\":\"Bad amount\",\"amount\":-5.0}";

        mockMvc.perform(post("/cart/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteCartTest_CartExists() throws Exception {
        given(cartService.deleteCart("1")).willReturn(true);

        mockMvc.perform(delete("/cart/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteCartTest_CartDoesNotExist() throws Exception {
        given(cartService.deleteCart("99")).willReturn(false);

        mockMvc.perform(delete("/cart/99")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}

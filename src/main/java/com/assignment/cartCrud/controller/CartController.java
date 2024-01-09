package com.assignment.cartCrud.controller;

import com.assignment.cartCrud.model.Cart;
import com.assignment.cartCrud.model.Product;
import com.assignment.cartCrud.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping
    public ResponseEntity<Cart> createCart() {
        Cart cart = cartService.createCart();
        return ResponseEntity.ok(cart);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cart> getCart(@PathVariable String id) {
        return cartService.getCart(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}")
    public ResponseEntity<Void> addProductToCart(@PathVariable String id, @RequestBody Product product) {
        if(!product.isValid()){
            return ResponseEntity.badRequest().build();
        }

        cartService.addProductToCart(id, product);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCart(@PathVariable String id) {
        cartService.deleteCart(id);
        return ResponseEntity.ok().build();
    }
}

package com.assignment.cartCrud.respository.hibernate;

import com.assignment.cartCrud.model.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "cart_products")
public class CartProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rowId;

    private Long productId;
    private String description;
    private Double amount;

    @ManyToOne(optional = false)
    private CartEntity cart;

    protected CartProductEntity() {
    }

    public CartProductEntity(Product product) {
        this.productId = product.id();
        this.description = product.description();
        this.amount = product.amount();
    }

    public void setCart(CartEntity cart) {
        this.cart = cart;
    }

    public Product toModel() {
        return new Product(productId, description, amount);
    }
}

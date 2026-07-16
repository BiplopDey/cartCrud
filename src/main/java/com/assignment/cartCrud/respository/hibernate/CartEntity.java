package com.assignment.cartCrud.respository.hibernate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
public class CartEntity {
    @Id
    private String id;

    private LocalDateTime creationTime;
    private LocalDateTime lastAccessedTime;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderColumn(name = "position")
    private List<CartProductEntity> products = new ArrayList<>();

    protected CartEntity() {
    }

    public CartEntity(String id, LocalDateTime creationTime, LocalDateTime lastAccessedTime) {
        this.id = id;
        this.creationTime = creationTime;
        this.lastAccessedTime = lastAccessedTime;
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public LocalDateTime getLastAccessedTime() {
        return lastAccessedTime;
    }

    public List<CartProductEntity> getProducts() {
        return products;
    }

    public void touch() {
        lastAccessedTime = LocalDateTime.now();
    }

    public void addProduct(CartProductEntity product) {
        product.setCart(this);
        products.add(product);
    }
}

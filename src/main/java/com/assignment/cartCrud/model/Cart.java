package com.assignment.cartCrud.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Cart {
    private String id;
    private List<Product> products;
    private LocalDateTime creationTime;

    @JsonIgnore
    private LocalDateTime lastAccessedTime;

    public Cart() {
        this.products = new ArrayList<>();
        this.lastAccessedTime = LocalDateTime.now();
    }

    public void updateLastAccessedTime() {
        this.lastAccessedTime = LocalDateTime.now();
    }

}

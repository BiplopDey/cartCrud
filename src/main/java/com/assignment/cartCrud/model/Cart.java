package com.assignment.cartCrud.model;

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

    public Cart() {
        this.products = new ArrayList<>();
    }

}

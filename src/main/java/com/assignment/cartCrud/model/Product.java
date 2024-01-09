package com.assignment.cartCrud.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
public class Product {
    private Long id;
    private String description;
    private Double amount;

    public boolean isValid(){
        return !Objects.isNull(id) && !Objects.isNull(amount);
    }
}

package com.assignment.cartCrud.model;

public record Product(Long id, String description, Double amount) {

    public boolean isValid(){
        return id != null
                && id > 0
                && amount != null
                && Double.isFinite(amount)
                && amount > 0;
    }
}

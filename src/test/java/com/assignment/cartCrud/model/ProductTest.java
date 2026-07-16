package com.assignment.cartCrud.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductTest {

    @Test
    void validatesProductIdentifiersAndAmounts() {
        assertTrue(new Product(1L, "Product", 10.0).isValid());
        assertFalse(new Product(null, "Product", 10.0).isValid());
        assertFalse(new Product(0L, "Product", 10.0).isValid());
        assertFalse(new Product(-1L, "Product", 10.0).isValid());
        assertFalse(new Product(1L, "Product", null).isValid());
        assertFalse(new Product(1L, "Product", 0.0).isValid());
        assertFalse(new Product(1L, "Product", -1.0).isValid());
        assertFalse(new Product(1L, "Product", Double.POSITIVE_INFINITY).isValid());
        assertFalse(new Product(1L, "Product", Double.NaN).isValid());
    }
}

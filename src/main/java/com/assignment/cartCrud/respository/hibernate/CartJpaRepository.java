package com.assignment.cartCrud.respository.hibernate;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface CartJpaRepository extends JpaRepository<CartEntity, String> {
    long deleteByLastAccessedTimeLessThanEqual(LocalDateTime expirationThreshold);
}

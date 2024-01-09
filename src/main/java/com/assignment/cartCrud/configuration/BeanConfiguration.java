package com.assignment.cartCrud.configuration;

import com.assignment.cartCrud.respository.CartInMemoryRepositoryImpl;
import com.assignment.cartCrud.respository.CartRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {
    @Bean
    public CartRepository getUserService() {
        return new CartInMemoryRepositoryImpl();
    }
}

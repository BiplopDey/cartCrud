package com.assignment.cartCrud.job;

import com.assignment.cartCrud.respository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

@Component
public class CartCleanupTask {
    @Autowired
    private CartRepository cartRepository;
    @Value("${cart.ttl.seconds}")
    private long ttlInSeconds;

    @Scheduled(fixedRateString = "${schedule.interval.milliseconds}")
    public void removeExpiredCarts() {
        StreamSupport.stream(Spliterators.spliteratorUnknownSize(cartRepository.getAllCarts(), 0), false)
                .filter(cart -> ChronoUnit.SECONDS.between(cart.getLastAccessedTime(), LocalDateTime.now()) >= ttlInSeconds)
                .forEach(cart -> cartRepository.deleteCart(cart.getId()));
    }
}

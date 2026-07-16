package com.assignment.cartCrud.job;

import com.assignment.cartCrud.respository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CartCleanupTask {
    @Autowired
    private CartRepository cartRepository;
    @Value("${cart.ttl.seconds}")
    private long ttlInSeconds;

    @Scheduled(fixedRateString = "${schedule.interval.milliseconds}")
    public void removeExpiredCarts() {
        LocalDateTime expirationThreshold = LocalDateTime.now().minusSeconds(ttlInSeconds);
        cartRepository.deleteExpiredCarts(expirationThreshold);
    }
}

package com.raj.wallet.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WalletCacheService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String PREFIX =
            "wallet:balance:";

    public BigDecimal getBalance(UUID walletId) {

        Object value =
                redisTemplate.opsForValue()
                        .get(PREFIX + walletId);

        if (value == null) {
            return null;
        }

        return new BigDecimal(value.toString());
    }

    public void saveBalance(
            UUID walletId,
            BigDecimal balance) {

        redisTemplate.opsForValue()
                .set(
                        PREFIX + walletId,
                        balance.toString(),
                        Duration.ofMinutes(10));
    }

    public void evictBalance(UUID walletId) {

        redisTemplate.delete(
                PREFIX + walletId);
    }
}
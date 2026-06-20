package com.raj.fraud.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    public Long incrementTransactionCount(
            String customerId) {

        String key =
                "customer:" + customerId + ":txnCount";

        Long count =
                redisTemplate.opsForValue().increment(key);

        redisTemplate.expire(
                key,
                Duration.ofMinutes(1));

        return count;
    }

    public BigDecimal updateDailyAmount(
            String customerId,
            BigDecimal amount) {

        String key =
                "customer:" + customerId + ":dailyAmount";

        Object existing =
                redisTemplate.opsForValue().get(key);

        BigDecimal total =
                existing == null
                        ? BigDecimal.ZERO
                        : new BigDecimal(existing.toString());

        total = total.add(amount);

        redisTemplate.opsForValue()
                .set(
                        key,
                        total,
                        Duration.ofDays(1));

        return total;
    }

    public void clearCustomerCache(
            String customerId) {

        redisTemplate.delete(
                "customer:" + customerId + ":txnCount");

        redisTemplate.delete(
                "customer:" + customerId + ":dailyAmount");
    }
}
package org.ohs.apitest.domain.concurrency.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class PostRedisRepository {
    private final RedisTemplate<String, String> lockRedisTemplate;

    public Boolean lock(String key) {
        String lockKey = lockKeyGen(key);

        // SETNX - Set If Not Exist
        // setIfAbsent() - 해당 Key가 존재하지 않을 때만 값을 설정 (3000 ms = 3초)
        return lockRedisTemplate.opsForValue()
                .setIfAbsent(lockKey, "lock", Duration.ofMillis(100L));
    }

    public Boolean unlock(String key) {
        return lockRedisTemplate.delete(lockKeyGen(key));
    }

    private String lockKeyGen(String key) {
        return "spin:" + key;
    }
}

package org.ohs.apitest.domain.concurrency;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class PostFacade {
    private final RedissonClient redissonClient;
    private final PostService postService;

    // 좋아요 증가 테스트 (With Distributed Lock)
    public ResponseEntity<?> likePostWithDistributedLock() {
        // 분산 락 KEY
        String lockKey = String.format("lock:like:post:%d", 1L);
        RLock lock = redissonClient.getLock(lockKey);

        try {
            // 락을 시도하고 일정 시간 동안 대기
            // 10초 동안 락을 시도하고, 락을 획득하면 1초 동안 유지
            boolean available = lock.tryLock(10, 1, TimeUnit.SECONDS);
            return postService.likePost();
        } catch (InterruptedException e) {
            return ResponseEntity.status(500).body("좋아요 실패: " + e.getMessage());
        } finally {
            // 스레드가 이 락을 보유하고 있는지, 있으면 Lock 해제
            if (lock.isHeldByCurrentThread()) { lock.unlock(); }
        }
    }
}

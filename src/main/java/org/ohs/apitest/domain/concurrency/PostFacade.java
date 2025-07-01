package org.ohs.apitest.domain.concurrency;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ohs.apitest.domain.concurrency.repository.PostRedisRepository;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostFacade {
    private final RedissonClient redissonClient;
    private final PostService postService;
    private final PostRedisRepository postRedisRepository;

    // 좋아요 증가 테스트 (With Distributed Lock (Use Redisson))
    public ResponseEntity<?> likePostWithDistributedLock() {
        // 분산 락 KEY
        String lockKey = String.format("lock:like:post:%d", 1L);
        RLock lock = redissonClient.getLock(lockKey);

        try {
            // (앞)초 동안 락을 시도하고, 락을 획득하면 (뒤)초 동안 유지
            if (!lock.tryLock(30, 3, TimeUnit.SECONDS)) {
                //log.warn("분산락 획득 실패: [key:{}] [thread:{}]", lockKey, Thread.currentThread().getName());
                return ResponseEntity.status(500).body("좋아요 실패: 락 획득 실패");
            }

            //long start = System.currentTimeMillis();
            ResponseEntity<?> response = postService.likePost();
            //long end = System.currentTimeMillis();
            //log.info("Spring 내부 비즈니스 로직 수행 시간: {}ms", (end - start));

            return response;
        } catch (InterruptedException e) {
            log.error(e.getMessage());
            return ResponseEntity.status(500).body("좋아요 실패: " + e.getMessage());
        } finally {
            // 스레드가 이 락을 보유하고 있는지, 있으면 Lock 해제
            if (lock.isHeldByCurrentThread()) { lock.unlock(); }
        }
    }

    // 좋아요 증가 테스트 (With Distributed Lock (Use Lettuce - Spin Lock))
//    public ResponseEntity<?> likePostWithSpinLock() {
//        // 분산 락 KEY
//        String lockKey = String.format("lock:like:post:%d", 1L);
//
//        while (!postRedisRepository.lock(lockKey)) {
//            try {
//                Thread.sleep(1);
//            } catch (InterruptedException e) {
//                // InterruptedException 발생 시, 현재 스레드를 깨우고 종료
//                Thread.currentThread().interrupt();
//                return ResponseEntity.status(500).body("좋아요 실패: " + e.getMessage());
//            }
//        }
//
//        ResponseEntity<?> res = postService.likePost();
//        postRedisRepository.unlock(lockKey);
//
//        return res;
//    }
}

package org.ohs.apitest;

import org.junit.jupiter.api.Test;
import org.ohs.apitest.domain.concurrency.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.concurrent.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class ConcurrencyTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostService postService;

    @Test
    void likePostWithSpinLock_concurrentTest() throws Exception {
        postService.initLikeCount(); // 테스트 전용으로 좋아요 수 초기화

        int threadCount = 500;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    mockMvc.perform(post("/api/post/like/lettuce/d-lock"))
                            .andExpect(status().isOk());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        System.out.println("총 소요 시간(ms): " + duration);
        System.out.println("정상적으로 요청된 개수: " + threadCount);
        assert latch.getCount() == 0 : "모든 요청이 정상적으로 완료되지 않았습니다.";
    }

    @Test
    void likePostWithDistributedLock_concurrentTest() throws Exception {
        postService.initLikeCount(); // 테스트 전용으로 좋아요 수 초기화

        int threadCount = 500;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        long startTime = System.currentTimeMillis();

        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                try {
                    mockMvc.perform(post("/api/post/like/redisson/d-lock"))
                            .andExpect(status().isOk());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        System.out.println("총 소요 시간(ms): " + duration);
        System.out.println("정상적으로 요청된 개수: " + threadCount);
        assert latch.getCount() == 0 : "모든 요청이 정상적으로 완료되지 않았습니다.";
    }
}

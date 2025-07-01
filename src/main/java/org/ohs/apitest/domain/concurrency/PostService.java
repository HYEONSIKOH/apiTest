package org.ohs.apitest.domain.concurrency;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ohs.apitest.domain.concurrency.entity.Post;
import org.ohs.apitest.domain.concurrency.repository.PostRepository;
import org.redisson.api.RedissonClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {
    private final PostRepository postRepository;

    // 테이블 데이터 없으면 생성
    private void initData() {
        Post post = new Post();
        post.setLikes(0);

        try {
            postRepository.save(post);
        } catch (Exception e) {
            log.error("Error initializing data: {}", e.getMessage());
        }
    }

    // 테스트 전용 (0으로 초기화)
    public void initLikeCount() {
        Post post = postRepository.findById(1L)
                .orElseThrow(() -> new NullPointerException("Post not found"));
        post.setLikes(0);
        postRepository.save(post);
    }

    // 좋아요 증가 테스트
    @Transactional
    public ResponseEntity<?> likePost() {
        if ( postRepository.count() == 0 ) initData();

        try {
            Post post = postRepository.findById(1L)
                    .orElseThrow(() -> new NullPointerException("Post not found"));
            post.setLikes(post.getLikes() + 1);
            postRepository.save(post);

            return ResponseEntity.ok().body("좋아요: " + post.getLikes());
        } catch (Exception e) {
            log.error("Error liking post: {}", e.getMessage());
            return ResponseEntity.status(500).body("좋아요 실패." + e.getMessage());
        }
    }

    // 좋아요 증가 테스트 (With Share Lock)
    @Transactional
    public ResponseEntity<?> likePostWithShareLock() {
        if ( postRepository.count() == 0 ) initData();

        try {
            Post post = postRepository.findByIdWithShareLock(1L)
                    .orElseThrow(() -> new NullPointerException("Post not found"));
            post.setLikes(post.getLikes() + 1);
            postRepository.save(post);

            return ResponseEntity.ok().body("좋아요: " + post.getLikes());
        } catch (Exception e) {
            log.error("Error liking post: {}", e.getMessage());
            return ResponseEntity.status(500).body("좋아요 실패." + e.getMessage());
        }
    }

    // 좋아요 증가 테스트 (With Exclusive Lock)
    @Transactional
    public ResponseEntity<?> likePostWithExclusiveLock() {
        if ( postRepository.count() == 0 ) initData();

        try {
            Post post = postRepository.findByIdWithExclusiveLock(1L)
                    .orElseThrow(() -> new NullPointerException("Post not found"));
            post.setLikes(post.getLikes() + 1);
            postRepository.save(post);

            return ResponseEntity.ok().body("좋아요: " + post.getLikes());
        } catch (Exception e) {
            log.error("Error liking post: {}", e.getMessage());
            return ResponseEntity.status(500).body("좋아요 실패." + e.getMessage());
        }
    }
}

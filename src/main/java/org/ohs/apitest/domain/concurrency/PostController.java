package org.ohs.apitest.domain.concurrency;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/post")
@RequiredArgsConstructor
@Tag(name = "동시성 테스트", description = "동시성 테스트를 위한, 좋아요 관리 Controller")
public class PostController {
    private final PostService postService;
    private final PostFacade postFacade;

    @PostMapping("/like")
    @Operation(
            summary = "좋아요 API",
            description = "좋아요 누르는 API"
    )
    public synchronized ResponseEntity<?> likePost() { return postService.likePost(); }

    @PostMapping("/like/s-lock")
    @Operation(
            summary = "좋아요 API",
            description = "(공유락) 좋아요 누르는 API"
    )
    public ResponseEntity<?> likePostWithShareLock() { return postService.likePostWithShareLock(); }

    @PostMapping("/like/x-lock")
    @Operation(
            summary = "좋아요 API",
            description = "(배타락) 좋아요 누르는 API"
    )
    public ResponseEntity<?> likePostWithExclusiveLock() { return postService.likePostWithExclusiveLock(); }

    @PostMapping("/like/redisson/d-lock")
    @Operation(
            summary = "좋아요 API",
            description = "(분산락 - Redisson) 좋아요 누르는 API"
    )
    public ResponseEntity<?> likePostWithDistributedLock() { return postFacade.likePostWithDistributedLock(); }

//    @PostMapping("/like/lettuce/d-lock")
//    @Operation(
//            summary = "좋아요 API",
//            description = "(분산락 - Lettuce) 좋아요 누르는 API"
//    )
//    public ResponseEntity<?> likePostWithSpinLock() { return postFacade.likePostWithSpinLock(); }
}

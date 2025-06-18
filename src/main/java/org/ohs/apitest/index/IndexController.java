package org.ohs.apitest.index;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/index")
@RequiredArgsConstructor
@Tag(name = "DB 인덱스 테스트", description = "인덱스 테스트를 위한 Controller")
public class IndexController {

    private final IndexService indexService;

    @PostMapping("/insert/dummy")
    @Operation(
            summary = "더미 데이터 저장 API",
            description = "1,000,000개의 더미 데이터 저장 (있으면 저장 안함)"
    )
    public ResponseEntity<?> insertDummyData() { return indexService.insertDummyData(); }

    @GetMapping("/user/email/{email}")
    @Operation(
            summary = "이메일로 사용자 조회 API",
            description = "이메일로 사용자 조회"
    )
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) { return indexService.getUserByEmail(email); }

    @GetMapping("/user/email/index/{email}")
    @Operation(
            summary = "이메일로 사용자 조회 API",
            description = "이메일로 사용자 조회 (인덱스 사용 버전)"
    )
    public ResponseEntity<?> getUserByEmailIndexVer(@PathVariable String email) { return indexService.getUserByEmailIndexVer(email); }

    @GetMapping("/user/email/contain/{str}")
    @Operation(
            summary = "이 문자를 포함하는 이메일 사용자 조회 API",
            description = "이메일로 사용자 조회"
    )
    public ResponseEntity<?> getUserByEmailVer2(@PathVariable String str) { return indexService.getUserByEmailVer2(str); }

    @GetMapping("/user/email/index/contain/{str}")
    @Operation(
            summary = "이 문자를 포함하는 이메일 사용자 조회 API",
            description = "이메일로 사용자 조회 (인덱스 사용 버전)"
    )
    public ResponseEntity<?> getUserByEmailIndexVer2(@PathVariable String str) { return indexService.getUserByEmailIndexVer2(str); }


}

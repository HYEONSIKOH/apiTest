package org.ohs.apitest.domain.index.repository;

import org.ohs.apitest.domain.index.entity.UserIndexVer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IndexRepository extends JpaRepository<UserIndexVer, Long> {

    // 이 문자를 포함한 이메일 찾기 (비교)
    Optional<List<UserIndexVer>> findByEmailContaining(String keyword);

    // 이메일로 사용자 찾기
    Optional<UserIndexVer> findByEmail(String email);
}

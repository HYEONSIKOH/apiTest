package org.ohs.apitest.domain.index.repository;

import org.ohs.apitest.domain.index.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoIndexRepository extends JpaRepository<User, Long> {

    // User 테이블 데이터 유무 확인
    @Query("SELECT COUNT(u) > 0 FROM User u")
    boolean existsByUserData();

    // 이 문자를 포함한 이메일 찾기 (비교)
    Optional<List<User>> findByEmailContaining(String keyword);

    Optional<User> findByEmail(String email);
}

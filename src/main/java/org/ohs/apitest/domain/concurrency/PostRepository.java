package org.ohs.apitest.domain.concurrency;

import jakarta.persistence.LockModeType;
import org.ohs.apitest.domain.concurrency.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findById(Long id);

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("SELECT P FROM Post P WHERE P.id = :id")
    Optional<Post> findByIdWithShareLock(@Param("id") Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT P FROM Post P WHERE P.id = :id")
    Optional<Post> findByIdWithExclusiveLock(@Param("id") Long id);
}

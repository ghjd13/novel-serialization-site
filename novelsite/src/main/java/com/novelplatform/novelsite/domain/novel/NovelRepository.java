package com.novelplatform.novelsite.domain.novel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface NovelRepository extends JpaRepository<Novel, Long> {

    /**
     * ID로 Novel을 찾을 때, 연관된 Member(createdBy)를 함께 조회(Fetch Join)합니다.
     * LazyInitializationException을 해결하기 위함입니다.
     */
    @Query("select n from Novel n join fetch n.createdBy m where n.id = :id")
    Optional<Novel> findByIdWithMember(@Param("id") Long id);
}

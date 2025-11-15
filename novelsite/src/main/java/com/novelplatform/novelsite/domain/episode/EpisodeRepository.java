package com.novelplatform.novelsite.domain.episode;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EpisodeRepository extends JpaRepository<Episode, Long> {

    // 소설 상세 페이지에서 회차 목록을 가져올 때 사용 (생성일 오름차순)
    List<Episode> findByNovelIdOrderByCreatedAtAsc(Long novelId);

    // 회차 상세 보기에서 소설 정보(제목 등)를 함께 가져오기 위해 Fetch Join 사용
    @Query("SELECT e FROM Episode e JOIN FETCH e.novel n WHERE e.id = :episodeId")
    Optional<Episode> findByIdWithNovel(@Param("episodeId") Long episodeId);
}

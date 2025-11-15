package com.novelplatform.novelsite.service;

import com.novelplatform.novelsite.domain.episode.Episode;
import com.novelplatform.novelsite.domain.episode.EpisodeRepository;
import com.novelplatform.novelsite.domain.novel.Novel;
import com.novelplatform.novelsite.web.episode.EpisodeCreateForm;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class EpisodeService {

    private final EpisodeRepository episodeRepository;
    private final NovelService novelService; // Novel을 찾고 권한을 확인하기 위해 주입

    public EpisodeService(EpisodeRepository episodeRepository, NovelService novelService) {
        this.episodeRepository = episodeRepository;
        this.novelService = novelService;
    }

    /**
     * 특정 소설의 모든 회차 목록을 조회합니다. (생성일 오름차순)
     */
    public List<Episode> findEpisodesForNovel(Long novelId) {
        return episodeRepository.findByNovelIdOrderByCreatedAtAsc(novelId);
    }

    /**
     * 회차 ID로 회차 정보를 조회합니다. (소설 정보 포함)
     */
    public Episode findEpisodeByIdWithNovel(Long episodeId) {
        return episodeRepository.findByIdWithNovel(episodeId)
            .orElseThrow(() -> new IllegalArgumentException("해당 회차를 찾을 수 없습니다. id=" + episodeId));
    }

    /**
     * 새로운 회차를 등록합니다.
     */
    @Transactional
    public Long createEpisode(Long novelId, EpisodeCreateForm form, String loginId) {
        // 1. 소설 조회
        Novel novel = novelService.findById(novelId);

        // 2. 권한 확인 (소설 작성자 본인만 회차 등록 가능)
        if (!novel.getCreatedBy().getLoginId().equals(loginId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "회차 등록 권한이 없습니다.");
        }

        // 3. 엔티티 생성 및 저장
        Episode episode = form.toEntity(novel);
        Episode savedEpisode = episodeRepository.save(episode);

        return savedEpisode.getId();
    }
}

package com.novelplatform.novelsite.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.novelplatform.novelsite.domain.member.Member;
import com.novelplatform.novelsite.domain.novel.Novel;
import com.novelplatform.novelsite.domain.novel.NovelRepository;
import com.novelplatform.novelsite.web.novel.NovelUpdateForm;

@Service
@Transactional(readOnly = true)
public class NovelService {

    private final NovelRepository novelRepository;
    private final FileStorageService fileStorageService;

    public NovelService(NovelRepository novelRepository, FileStorageService fileStorageService) {
        this.novelRepository = novelRepository;
        this.fileStorageService = fileStorageService;
    }

    @Transactional
    public Long createNovel(String title, String author, String description, MultipartFile coverImage, Member member) {
        String storedPath = fileStorageService.store(coverImage);
        Novel novel = new Novel(title, author, description, storedPath, member);
        Novel saved = novelRepository.save(novel);
        return saved.getId();
    }

    public List<Novel> findAll() {
        return novelRepository.findAll();
    }

    /**
     * 소설 ID로 소설을 찾습니다. (수정됨: Fetch Join 사용)
     * @param novelId 소설 ID
     * @return Novel 엔티티 (Member 정보 포함)
     * @throws IllegalArgumentException 소설을 찾지 못한 경우
     */
    public Novel findById(Long novelId) {
        // 기존 findById 대신, Member를 함께 가져오는 findByIdWithMember를 사용합니다.
        return novelRepository.findByIdWithMember(novelId)
            .orElseThrow(() -> new IllegalArgumentException("해당 소설을 찾을 수 없습니다. id=" + novelId));
    }

    /**
     * 소설 정보 수정
     * @param novelId 수정할 소설 ID
     * @param form 수정할 정보가 담긴 DTO
     * @param loginId 현재 로그인한 사용자 ID (권한 체크용)
     */
    @Transactional
    public void updateNovel(Long novelId, NovelUpdateForm form, String loginId) {
        Novel novel = findById(novelId); // 이 시점에 Member 정보도 이미 로드됨
        // 권한 체크
        if (!novel.getCreatedBy().getLoginId().equals(loginId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "수정 권한이 없습니다.");
        }
        // 엔티티 내부 로직을 통해 업데이트
        novel.update(form.getTitle(), form.getAuthor(), form.getDescription());
    }

    /**
     * 소설 삭제
     * @param novelId 삭제할 소설 ID
     * @param loginId 현재 로그인한 사용자 ID (권한 체크용)
     */
    @Transactional
    public void deleteNovel(Long novelId, String loginId) {
        Novel novel = findById(novelId); // 이 시점에 Member 정보도 이미 로드됨
        // 권한 체크
        if (!novel.getCreatedBy().getLoginId().equals(loginId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "삭제 권한이 없습니다.");
        }

        // 1. 이미지 파일 먼저 삭제
        fileStorageService.delete(novel.getCoverImagePath());

        // 2. 소설 엔티티 삭제
        novelRepository.delete(novel);
    }
}


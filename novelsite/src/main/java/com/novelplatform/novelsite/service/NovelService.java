package com.novelplatform.novelsite.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.novelplatform.novelsite.domain.member.Member;
import com.novelplatform.novelsite.domain.novel.Novel;
import com.novelplatform.novelsite.domain.novel.NovelRepository;

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
}

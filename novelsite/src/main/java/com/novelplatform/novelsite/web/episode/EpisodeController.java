package com.novelplatform.novelsite.web.episode;

import com.novelplatform.novelsite.domain.episode.Episode;
import com.novelplatform.novelsite.domain.novel.Novel;
import com.novelplatform.novelsite.service.EpisodeService;
import com.novelplatform.novelsite.service.NovelService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
// URL을 /novels/{novelId}/episodes 로 공통화
@RequestMapping("/novels/{novelId}/episodes")
public class EpisodeController {

    private final EpisodeService episodeService;
    private final NovelService novelService;

    public EpisodeController(EpisodeService episodeService, NovelService novelService) {
        this.episodeService = episodeService;
        this.novelService = novelService;
    }

    /**
     * 회차 등록 폼
     */
    @GetMapping("/new")
    public String createEpisodeForm(@PathVariable Long novelId, Model model, @AuthenticationPrincipal User user) {
        // NovelController의 checkAuthority를 재사용하기 위해 Novel 엔티티 조회
        // (서비스 계층에서 권한 검사를 하므로 여기서는 폼 전달 목적)
        Novel novel = novelService.findById(novelId);

        model.addAttribute("novelId", novelId);
        model.addAttribute("novelTitle", novel.getTitle());
        model.addAttribute("episodeCreateForm", new EpisodeCreateForm());
        return "episodes/create-form";
    }

    /**
     * 회차 등록 처리
     */
    @PostMapping("/new")
    public String createEpisode(@PathVariable Long novelId,
                                @Valid @ModelAttribute EpisodeCreateForm episodeCreateForm,
                                BindingResult bindingResult,
                                @AuthenticationPrincipal User user, Model model) {

        if (bindingResult.hasErrors()) {
            Novel novel = novelService.findById(novelId);
            model.addAttribute("novelId", novelId);
            model.addAttribute("novelTitle", novel.getTitle());
            return "episodes/create-form"; // 유효성 검사 실패 시 폼으로 복귀
        }

        // 서비스 계층에서 권한 검사 및 저장
        episodeService.createEpisode(novelId, episodeCreateForm, user.getUsername());

        // 등록 완료 후 소설 상세 페이지로 리다이렉트
        return "redirect:/novels/" + novelId;
    }

    /**
     * 회차 상세 보기 (내용 읽기)
     */
    @GetMapping("/{episodeId}")
    public String episodeDetail(@PathVariable Long novelId, @PathVariable Long episodeId, Model model) {
        Episode episode = episodeService.findEpisodeByIdWithNovel(episodeId);

        // URL의 novelId와 실제 episode의 novelId가 일치하는지 간단히 확인 (선택 사항)
        if (!episode.getNovel().getId().equals(novelId)) {
            throw new IllegalArgumentException("잘못된 접근입니다.");
        }

        model.addAttribute("episode", episode);
        return "episodes/detail-view";
    }
}

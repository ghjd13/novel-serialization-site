package com.novelplatform.novelsite.web.novel;

import com.novelplatform.novelsite.domain.episode.Episode; // [추가]
import com.novelplatform.novelsite.domain.novel.Novel;
import com.novelplatform.novelsite.service.EpisodeService; // [추가]
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.novelplatform.novelsite.domain.member.Member;
import com.novelplatform.novelsite.service.MemberService;
import com.novelplatform.novelsite.service.NovelService;

import jakarta.validation.Valid;
import org.springframework.web.server.ResponseStatusException;

import java.util.List; // [추가]

@Controller
public class NovelController {

    private final NovelService novelService;
    private final MemberService memberService;
    private final EpisodeService episodeService; // [추가]

    public NovelController(NovelService novelService, MemberService memberService, EpisodeService episodeService) { // [수정]
        this.novelService = novelService;
        this.memberService = memberService;
        this.episodeService = episodeService; // [추가]
    }

    @GetMapping("/novels")
    public String list(Model model) {
        model.addAttribute("novels", novelService.findAll());
        return "novels/list";
    }

    /**
     * 소설 상세 페이지
     */
    @GetMapping("/novels/{novelId}")
    public String detail(@PathVariable Long novelId, Model model) {
        // [수정] 소설 정보와 함께 회차 목록도 가져옴
        Novel novel = novelService.findById(novelId);
        List<Episode> episodes = episodeService.findEpisodesForNovel(novelId);

        model.addAttribute("novel", novel);
        model.addAttribute("episodes", episodes); // [추가]
        return "novels/detail";
    }

    @GetMapping("/novels/new")
    public String createForm(Model model) {
        model.addAttribute("novelCreateForm", new NovelCreateForm());
        return "novels/create";
    }

    @PostMapping("/novels/new")
    public String create(@AuthenticationPrincipal User user, @Valid @ModelAttribute NovelCreateForm novelCreateForm,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "novels/create";
        }

        Member member = getAuthenticatedMember(user);
        if (member == null) {
            bindingResult.reject("member.notfound", "회원 정보를 찾을 수 없습니다. 다시 로그인 해주세요.");
            return "novels/create";
        }

        novelService.createNovel(novelCreateForm.getTitle(), novelCreateForm.getAuthor(),
            novelCreateForm.getDescription(), novelCreateForm.getCoverImage(), member);
        return "redirect:/novels";
    }

    /**
     * 소설 수정 폼
     */
    @GetMapping("/novels/{novelId}/edit")
    public String updateForm(@PathVariable Long novelId, Model model, @AuthenticationPrincipal User user) {
        Novel novel = novelService.findById(novelId);
        // 권한 체크
        checkAuthority(novel, user);

        model.addAttribute("novelUpdateForm", new NovelUpdateForm(novel));
        model.addAttribute("novelId", novelId); // 폼 action에서 사용할 ID
        return "novels/editForm";
    }

    /**
     * 소설 수정 처리
     */
    @PostMapping("/novels/{novelId}/edit")
    public String update(@PathVariable Long novelId, @Valid @ModelAttribute NovelUpdateForm novelUpdateForm,
                         BindingResult bindingResult, @AuthenticationPrincipal User user) {

        if (bindingResult.hasErrors()) {
            return "novels/editForm";
        }

        novelService.updateNovel(novelId, novelUpdateForm, user.getUsername());
        return "redirect:/novels/" + novelId; // 수정 후 상세 페이지로 리다이렉트
    }

    /**
     * 소설 삭제 처리
     */
    @PostMapping("/novels/{novelId}/delete")
    public String delete(@PathVariable Long novelId, @AuthenticationPrincipal User user) {
        novelService.deleteNovel(novelId, user.getUsername());
        return "redirect:/novels"; // 삭제 후 목록으로 리다이렉트
    }


    // === 편의 메서드 === //

    /**
     * 현재 로그인한 사용자의 Member 엔티티를 반환합니다.
     */
    private Member getAuthenticatedMember(User user) {
        if (user == null) {
            return null;
        }
        return memberService.findByLoginId(user.getUsername());
    }

    /**
     * 현재 로그인한 사용자가 해당 소설의 작성자인지 확인합니다.
     */
    private void checkAuthority(Novel novel, User user) {
        if (user == null || !novel.getCreatedBy().getLoginId().equals(user.getUsername())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "권한이 없습니다.");
        }
    }
}

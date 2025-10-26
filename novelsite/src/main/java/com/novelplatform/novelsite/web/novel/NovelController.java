package com.novelplatform.novelsite.web.novel;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.novelplatform.novelsite.domain.member.Member;
import com.novelplatform.novelsite.service.MemberService;
import com.novelplatform.novelsite.service.NovelService;

import jakarta.validation.Valid;

@Controller
public class NovelController {

    private final NovelService novelService;
    private final MemberService memberService;

    public NovelController(NovelService novelService, MemberService memberService) {
        this.novelService = novelService;
        this.memberService = memberService;
    }

    @GetMapping("/novels")
    public String list(Model model) {
        model.addAttribute("novels", novelService.findAll());
        return "novels/list";
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

        if (user == null) {
            bindingResult.reject("member.notfound", "로그인이 필요한 기능입니다.");
            return "novels/create";
        }

        Member member = memberService.findByLoginId(user.getUsername());
        if (member == null) {
            bindingResult.reject("member.notfound", "회원 정보를 찾을 수 없습니다. 다시 로그인 해주세요.");
            return "novels/create";
        }

        novelService.createNovel(novelCreateForm.getTitle(), novelCreateForm.getAuthor(),
                novelCreateForm.getDescription(), novelCreateForm.getCoverImage(), member);
        return "redirect:/novels";
    }
}

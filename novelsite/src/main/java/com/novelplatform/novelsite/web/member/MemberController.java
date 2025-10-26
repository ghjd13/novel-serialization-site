package com.novelplatform.novelsite.web.member;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.novelplatform.novelsite.service.MemberService;

import jakarta.validation.Valid;

@Controller
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/members/register")
    public String registerForm(Model model) {
        model.addAttribute("memberRegisterForm", new MemberRegisterForm());
        return "members/register";
    }

    @PostMapping("/members/register")
    public String register(@Valid @ModelAttribute MemberRegisterForm memberRegisterForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "members/register";
        }

        try {
            memberService.register(memberRegisterForm.toEntity());
        } catch (IllegalArgumentException e) {
            bindingResult.rejectValue("loginId", "duplicate", e.getMessage());
            return "members/register";
        }

        return "redirect:/login?registered";
    }
}

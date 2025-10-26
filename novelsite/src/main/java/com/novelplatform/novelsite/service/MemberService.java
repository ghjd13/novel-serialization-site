package com.novelplatform.novelsite.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.novelplatform.novelsite.domain.member.Member;
import com.novelplatform.novelsite.domain.member.MemberRepository;

@Service
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Long register(Member member) {
        validateDuplicateLoginId(member.getLoginId());
        member.updatePassword(passwordEncoder.encode(member.getPassword()));
        Member saved = memberRepository.save(member);
        return saved.getId();
    }

    public Member findByLoginId(String loginId) {
        return memberRepository.findByLoginId(loginId).orElse(null);
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    private void validateDuplicateLoginId(String loginId) {
        if (memberRepository.existsByLoginId(loginId)) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }
    }
}

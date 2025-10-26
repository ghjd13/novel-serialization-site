package com.novelplatform.novelsite.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.novelplatform.novelsite.domain.member.Member;
import com.novelplatform.novelsite.service.MemberService;

@Service
public class MemberUserDetailsService implements UserDetailsService {

    private final MemberService memberService;

    public MemberUserDetailsService(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberService.findByLoginId(username);
        if (member == null) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }

        return User.withUsername(member.getLoginId())
                .password(member.getPassword())
                .roles(member.getRole().name())
                .build();
    }
}

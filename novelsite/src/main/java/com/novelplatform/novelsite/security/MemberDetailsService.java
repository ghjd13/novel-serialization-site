package com.novelplatform.novelsite.security;

import com.novelplatform.novelsite.domain.member.Member;
import com.novelplatform.novelsite.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member m = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        // Member.Role 이 AUTHOR/ADMIN/READER 라고 가정 (없다면 "USER" 등으로)
        return User.builder()
                .username(m.getUsername())
                .password(m.getPassword())           // 반드시 BCrypt로 저장되어 있어야 함
                .roles(m.getRole().name())           // ROLE_ 접두어 자동 부여
                .build();
    }

}

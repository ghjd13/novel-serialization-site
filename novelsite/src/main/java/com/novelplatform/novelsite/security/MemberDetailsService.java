package com.novelplatform.novelsite.security;

import com.novelplatform.novelsite.domain.member.Member;
import com.novelplatform.novelsite.repository.MemberRepository;
import java.util.Objects;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public MemberDetailsService(MemberRepository memberRepository) {
        this.memberRepository = Objects.requireNonNull(memberRepository);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Member not found: " + username));
        return new User(
                member.getUsername(),
                member.getPassword(),
                member.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                        .toList());
    }
}

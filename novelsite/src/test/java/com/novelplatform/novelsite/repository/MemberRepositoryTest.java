package com.novelplatform.novelsite.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.novelplatform.novelsite.domain.member.Member;
import com.novelplatform.novelsite.domain.member.MemberRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @DisplayName("회원 저장 후 username 으로 조회할 수 있다")
    @Test
    void saveAndFindByUsername() {
        // given
        Member member = Member.createAuthor("author1", passwordEncoder.encode("password"));

        // when
        memberRepository.save(member);
        Member found = memberRepository.findByUsername("author1").orElseThrow();

        // then
        assertThat(found.getId()).isNotNull();
        assertThat(found.getRoles()).contains(MemberRole.AUTHOR);
    }
}

package com.novelplatform.novelsite.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.novelplatform.novelsite.domain.member.Address;
import com.novelplatform.novelsite.domain.member.Member;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Test
    void 회원가입과_중복체크() {
        Member member = new Member("testuser", "테스터", "password", new Address("서울", "강남대로", "12345"));
        Long id = memberService.register(member);

        assertThat(id).isNotNull();

        Member duplicated = new Member("testuser", "다른이름", "password", new Address("서울", "테스트", "54321"));
        assertThatThrownBy(() -> memberService.register(duplicated))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 사용 중인 아이디");
    }
}

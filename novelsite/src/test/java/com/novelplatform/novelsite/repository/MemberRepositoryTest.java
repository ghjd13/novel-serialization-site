package com.novelplatform.novelsite.repository;

import com.novelplatform.novelsite.domain.member.Member;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    void saveAndRollbackVerificationTest() {
        String testName = "TestMemberrA";
        String testPassword = "test_password_1234";
        Member memberToSave = new Member(testName, testPassword);

        Member savedMember = memberRepository.save(memberToSave);
        Long savedId = savedMember.getId();

        // Then (검증) [5]
        // 1. 저장된 엔티티가 null이 아닌지 확인
        assertThat(savedId).isNotNull();

        // 2. ID로 조회하여 저장된 데이터가 일치하는지 확인
        Optional<Member> foundMember = memberRepository.findById(savedId);

        // Optional이 존재하며, 이름이 일치하는지 검증
        assertThat(foundMember).isPresent();
        assertThat(foundMember.get().getName()).isEqualTo(testName);
        assertThat(foundMember.get().getPassword()).isEqualTo(testPassword);
        // 3. @Transactional 롤백 검증 (자동 수행)
        // 이 테스트는 @Transactional 덕분에 성공 후 DB에 저장된 내용이 자동으로 제거됩니다.
    }
    @Test
    void findByNameTest() {
        // Given (준비)
        Member member1 = new Member("UserB", "pass1");
        Member member2 = new Member("UserC", "pass2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        // When (실행)
        List<Member> foundList = memberRepository.findByName("UserB");

        // Then (검증)
        assertThat(foundList.size()).isEqualTo(1);
        assertThat(foundList.get(0).getName()).isEqualTo("UserB");
    }
}

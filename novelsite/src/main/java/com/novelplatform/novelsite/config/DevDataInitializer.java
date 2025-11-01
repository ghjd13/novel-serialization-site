package com.novelplatform.novelsite.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.novelplatform.novelsite.domain.member.Address;
import com.novelplatform.novelsite.domain.member.Member;
import com.novelplatform.novelsite.domain.novel.Novel;
import com.novelplatform.novelsite.service.MemberService;
import com.novelplatform.novelsite.service.NovelService;

@Configuration
@Profile("dev") // "dev" 프로필이 활성화될 때만 이 클래스가 실행됩니다.
public class DevDataInitializer {

    @Bean
    public CommandLineRunner initData(MemberService memberService, NovelService novelService) {
        return args -> {
            // 1. 테스트용 회원 2명 생성 (비밀번호는 MemberService에서 암호화됨)
            // 사용자 1 (아이디: user, 비밀번호: 123456)
            Member member1 = new Member("user", "테스트유저", "123456", new Address("서울", "강남대로", "12345"));
            memberService.register(member1);

            // 사용자 2 (아이디: admin, 비밀번호: 123456)
            Member member2 = new Member("admin", "관리자", "123456", new Address("부산", "해운대로", "54321"));
            memberService.register(member2);

            // 2. 테스트용 소설 2개 생성 (이미지(MultipartFile)는 null로 전달)
            Member user = memberService.findByLoginId("user");
            Member admin = memberService.findByLoginId("admin");

            // 소설 1 (작성자: user)
            novelService.createNovel(
                "화산귀환",
                "비가",
                "전쟁 영웅이 100년 뒤 망해버린 자기 문파의 아이로 환생했다!",
                null, // 이미지는 없음
                user);

            // 소설 2 (작성자: admin)
            novelService.createNovel(
                "전지적 독자 시점",
                "싱숑",
                "어느 날, 내가 읽던 소설의 내용대로 세상이 멸망하기 시작했다.",
                null, // 이미지는 없음
                admin);
        };
    }
}

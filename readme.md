# Spring Boot JPA Novel Serialization Site

> Java 21 / Spring Boot 3.5 기반으로 소설 연재 플랫폼을 구축하기 위한 기반 프로젝트입니다. 현재는 최소한의 인증 및 회원 저장소만 갖춘 스켈레톤 상태이며, [ROADMAP](./ROADMAP.md)에 따라 기능을 단계적으로 확장할 예정입니다.

## 1. 현재 구현 현황

| 영역 | 주요 구성 요소 | 비고 |
| --- | --- | --- |
| 애플리케이션 부트스트랩 | `NovelsiteApplication` | Spring Boot 실행 진입점만 구성되어 있습니다. |
| 도메인/저장소 | `Member` 엔티티, `MemberRepository` | 이름·비밀번호를 가지는 단순 회원 도메인과 JPA 저장소를 제공합니다. |
| 인증/보안 | `SecurityConfig`, `MemberDetailsService`, `LoginController` | 폼 로그인, BCrypt 기반 비밀번호 인코더, 로그인 페이지 뼈대를 정의했습니다. |
| 테스트 | `MemberRepositoryTest`, `NovelsiteApplicationTests` | 저장/조회 및 컨텍스트 로딩 검증만 포함되어 있습니다. |
| 빌드 스크립트 | `build.gradle` | Spring Web/JPA/Security, Querydsl, H2/MySQL, Lombok 등을 의존성으로 선언했습니다. |

### 한계 및 향후 보완 계획
- Querydsl, 복합 검색, 권한 기반 페이지 접근 등은 아직 구현되지 않았습니다.
- 테스트 커버리지는 저장소 단위 테스트에 국한되어 있으며, 통합 테스트/커버리지 게이트는 미구성 상태입니다.
- 배포/운영 자동화(CI/CD, AWS 인프라 등)는 준비 단계로, 향후 Sprint 5에서 다룹니다.

## 2. 기술 스택 요약
- **언어/런타임:** Java 21 (Gradle Toolchain 사용)
- **프레임워크:** Spring Boot 3.5.6, Spring Data JPA, Spring Security, Spring MVC, Thymeleaf
- **데이터베이스:** MySQL (운영 계획), H2 (개발/테스트), Querydsl 5.x (동적 조회 계획)
- **빌드/품질:** Gradle 8.x, JUnit 5, Spring Security Test, Lombok
- **향후 도입 예정:** Testcontainers MySQL, Flyway, JaCoCo, Spotless, OpenTelemetry 등 — 세부 일정은 로드맵을 참고하세요.

## 3. 디렉터리 구조
```text
novelsite/
├── src/main/java/com/novelplatform/novelsite
│   ├── NovelsiteApplication.java
│   ├── config/            # Spring Security 설정
│   ├── domain/member/     # Member 엔티티
│   ├── repository/        # MemberRepository
│   ├── security/          # UserDetailsService 구현체
│   └── web/               # 로그인 컨트롤러(폼 뼈대)
└── src/test/java/com/novelplatform/novelsite
    ├── NovelsiteApplicationTests.java
    └── repository/MemberRepositoryTest.java
```

## 4. 로컬 실행 방법
1. **사전 준비**
   - JDK 21 이상 설치 (Gradle Wrapper가 자동 사용)
   - 로컬 개발 시 H2(메모리 DB)를 기본으로 사용합니다.
2. **애플리케이션 실행**
   ```bash
   cd novelsite
   ./gradlew bootRun
   ```
   - Querydsl Q 클래스는 `./gradlew build` 실행 시 생성됩니다.
3. **테스트 실행**
   ```bash
   ./gradlew test
   ```
   - 현재는 JPA 저장소와 애플리케이션 컨텍스트 로딩 검증만 포함되어 있습니다.
4. **패키징**
   ```bash
   ./gradlew clean bootJar
   ```
   - 결과물은 `novelsite/build/libs/`에 생성됩니다.

## 5. 향후 설계 방향 (요약)
- **아키텍처:** Presentation → Application → Domain ← Infrastructure 계층 분리, 도메인 이벤트, Querydsl 기반 조회, Keyset 페이지네이션 도입
- **도메인 모델:** Member, Novel, Episode, Comment, Bookmark를 중심으로 애그리게이트 설계 및 제약 정의
- **테스트 전략:** 단위/슬라이스/통합 테스트 조합, Testcontainers MySQL, JaCoCo 커버리지 게이트
- **배포/운영:** Docker 기반 빌드, GitHub Actions CI/CD, AWS(EC2/ECS + RDS + S3), 관측성(OpenTelemetry, Prometheus, Grafana)
- **로드맵:** Sprint 0~5까지 기반 구축 → 인증/도메인 구현 → 조회/커뮤니티 → 배포 자동화 순으로 확장 (상세 일정은 ROADMAP 참조)

자세한 일정과 마일스톤은 [ROADMAP.md](./ROADMAP.md)에 기록되어 있으며, 문서와 실제 구현은 Sprint 단위로 동기화할 예정입니다.

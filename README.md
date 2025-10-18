# 소설 연재 서비스 기술 요약

## 목차
1. [주요 기능 구현 및 기술적 차별점](#iv-주요-기능-구현-및-기술적-차별점)
2. [테스트 및 품질 관리 전략](#v-테스트-및-품질-관리-전략)
3. [배포 및 운영 환경](#vi-배포-및-운영-환경-infra--devops)
4. [기술 스택 및 프로젝트 구조](#vii-기술-스택-및-프로젝트-구조)
5. [로컬 개발 및 실행 가이드](#viii-로컬-개발-및-실행-가이드)
6. [운영 팁 및 향후 고도화 아이디어](#ix-운영-팁-및-향후-고도화-아이디어)
7. [로드맵](#x-로드맵)

## IV. 주요 기능 구현 및 기술적 차별점

### 1. 동적 검색 및 성능 최적화 (Querydsl 활용)
- **문제:** 제목, 장르, 작성자, 상태 등 복잡한 검색 조건을 JPQL 문자열로 조합할 때 타입 안전성이 떨어지고 코드가 복잡해지는 문제가 있었습니다.
- **해결:** Querydsl 기반의 `BooleanExpression` 조건 메서드를 정의하여 `where()` 절에 쉼표로 나열했습니다. null 조건은 자동으로 무시되고, 컴파일 시점에 오류를 감지할 수 있어 안전한 동적 쿼리를 구현했습니다.

### 2. 인증 및 인가 (Spring Security / 인터셉터)
- **인증:** `UserDetailsService`를 구현해 데이터베이스 기반의 세션 로그인 환경을 구축했습니다.
- **인가:** 작가 전용 페이지(`author/**`) 접근 시 스프링 인터셉터로 로그인 여부와 작가 권한을 검증하도록 로직을 분리해 적용했습니다.

### 3. 데이터 유효성 및 견고한 폼 처리
- **전략:** 등록(`NovelSaveForm`)과 수정(`NovelUpdateForm`) 요구사항이 충돌하는 문제를 해결하기 위해 도메인 엔티티와 폼 전송용 DTO를 분리했습니다.
- **기술:** DTO에 `@NotBlank`, `@Range` 등의 Bean Validation 애노테이션을 적용해 선언적으로 유효성 검증을 수행했습니다.

## V. 테스트 및 품질 관리 전략

### 1. 테스트 격리 (Isolation)
- 모든 통합 테스트 메서드에 `@Transactional`을 적용해 테스트 종료 직후 데이터베이스 변경 사항을 자동으로 롤백하여 독립성을 확보했습니다.

### 2. 코드 품질 및 설계 검증
- 주문 취소 시 재고를 복구하는 `order.cancel()`과 같은 핵심 비즈니스 로직을 엔티티 내부에서 처리하고 테스트 코드로 검증했습니다.
- JaCoCo로 instruction ≥ 70%, branch ≥ 60%의 커버리지 목표를 설정해 품질을 관리했습니다.

## VI. 배포 및 운영 환경 (Infra & DevOps)

| 영역 | 기술 / 도구 | 상세 구현 사항 | 비고 |
| --- | --- | --- | --- |
| 빌드/패키징 | Fat Jar (Executable Jar) | WAS를 내장한 단일 실행 가능 JAR 파일로 빌드하여 배포 복잡성을 줄였습니다. | |
| CI/CD | GitHub Actions | 빌드, 테스트, 정적 분석, 배포를 자동화하는 파이프라인을 구성했습니다. | |
| 클라우드 인프라 | AWS EC2 | 실제 서비스와 유사한 환경을 위해 EC2에 배포했습니다. | |
| 네트워크 | Elastic IP | EC2 인스턴스 재기동 시 퍼블릭 IP 변경을 막기 위해 고정 공인 IP를 할당했습니다. | |
| 비용 관리 | EC2 중지 전략 | 프리티어 기간 만료 후 요금 청구를 방지하기 위해 사용하지 않을 때 인스턴스를 중지합니다. | 계획 중 |

## VII. 기술 스택 및 프로젝트 구조

### 사용 기술 요약
- **언어 & 프레임워크:** Java 21, Spring Boot 3.5, Spring Security, Spring Data JPA, Thymeleaf
- **빌드 도구:** Gradle (Wrapper 포함)
- **데이터베이스:** MySQL (운영), H2 (테스트 및 로컬 인메모리 환경)
- **형상 관리 & 품질:** GitHub Actions, JaCoCo, Querydsl

### 디렉터리 개요
```
novelsite/
├── src/main/java/com/novelplatform/novelsite
│   ├── config          # Security, Querydsl 설정 등 애플리케이션 전역 설정
│   ├── domain          # JPA 엔티티 및 핵심 비즈니스 로직
│   ├── repository      # Querydsl과 함께 사용하는 JPA Repository
│   ├── security        # UserDetailsService 구현 및 인증/인가 컴포넌트
│   └── web             # 컨트롤러, DTO(Form) 계층
└── src/test/java       # 통합/단위 테스트
```

## VIII. 로컬 개발 및 실행 가이드

1. **환경 준비**
   - JDK 21 이상 설치 (Gradle Wrapper가 자동으로 감지)
   - 로컬 개발 시 H2 데이터베이스를 기본으로 사용하며, MySQL 연결 정보는 `application-*.yml`에서 관리합니다.

2. **애플리케이션 실행**
   ```bash
   cd novelsite
   ./gradlew bootRun
   ```
   - Querydsl Q 클래스는 Gradle 빌드 과정에서 자동 생성됩니다.

3. **테스트 실행**
   ```bash
   ./gradlew test
   ```
   - `@Transactional`을 활용한 테스트 격리 덕분에, 테스트 간 데이터 간섭 없이 반복 실행이 가능합니다.

4. **패키징**
   ```bash
   ./gradlew clean bootJar
   ```
   - 생성된 Fat Jar는 `build/libs/` 경로에 위치하며, EC2 등 배포 환경에서 바로 실행할 수 있습니다.

## IX. 운영 팁 및 향후 고도화 아이디어
- **모니터링 연계:** CloudWatch나 Grafana와 연동하여 EC2 인스턴스의 리소스 사용량을 시각화하는 방안을 고려하고 있습니다.
- **캐시 도입:** Redis 기반의 캐싱을 추가하여 조회량이 많은 소설 상세/목록 API의 응답 속도를 개선할 수 있습니다.
- **CI 품질 확장:** GitHub Actions 파이프라인에 정적 분석(SpotBugs, Checkstyle 등)을 추가해 품질 게이트를 강화할 수 있습니다.

## X. 로드맵
- 보다 상세한 연도별·분기별 추진 계획과 마일스톤은 [`ROADMAP.md`](ROADMAP.md)에서 확인할 수 있습니다.

# 웹소설 연재 사이트 개발 프로젝트 기획서 (학원생 기준)

## 1. 프로젝트 개요
### 1.1 프로젝트 목표 및 범위
본 프로젝트는 Spring Boot를 기반으로 한 웹소설 연재 사이트의 최소 기능 제품(MVP)을 8주 안에 구현하는 것을 목표로 합니다. 학원 또는 부트캠프에서 Spring Boot와 JPA를 처음 접하는 학습자를 대상으로 하며, 실습을 통해 실무 감각을 익히도록 설계되었습니다.

- **목표:** 회원 관리와 소설 등록/관리를 중심으로 CRUD, 보안, 파일 업로드의 기초를 학습합니다.
- **핵심 기능 범위:**
  - 회원 가입 및 로그인 (Spring Security 기반)
  - 소설 등록, 수정, 삭제, 목록/상세 보기
  - 표지 이미지 업로드 및 관리 (로컬 저장)
- **기술 스택:** Spring Boot, Spring Data JPA, Spring Security, H2/MySQL, Thymeleaf, Bootstrap
- **배포:** 로컬 실행을 기본으로 하며, 선택적으로 Heroku 등 간단한 클라우드 배포를 경험할 수 있습니다.
- **학습 포인트:** Spring Boot 프로젝트 구조, JPA 엔티티/리포지토리 작성, 기본 보안 설정, 파일 업로드 처리, 로컬 개발 환경 구성.

## 2. 시스템 아키텍처 및 기술 스택
### 2.1 기술 스택 선정 및 역할
| 구분 | 기술 | 역할 및 선정 이유 | 학습 포인트 |
| --- | --- | --- | --- |
| 백엔드 프레임워크 | Spring Boot | 내장 톰캣으로 빠른 시작, 자동 설정으로 초보자도 환경 구성 용이 | 애플리케이션 구조 파악, 의존성 관리 |
| 데이터베이스 | H2(개발/테스트), MySQL(운영) | H2로 빠른 로컬 개발, MySQL로 실 DB 경험 | DB 프로필 전환, SQL 콘솔 사용 |
| 데이터 접근 | Spring Data JPA | CRUD 자동화, 쿼리 부담 완화 | 엔티티 매핑, 쿼리 메소드 작성 |
| 인증/인가 | Spring Security | 기본 로그인/세션 관리 제공 | Security 필터 체인 이해, 비밀번호 암호화 |
| 뷰 템플릿 | Thymeleaf | HTML과 연동이 쉬워 서버 렌더링 학습에 적합 | 컨트롤러-뷰 데이터 바인딩 |
| 프론트엔드 | Bootstrap | CSS 작성 부담 감소, 반응형 UI 손쉬운 구현 | 반응형 레이아웃, 컴포넌트 활용 |
| 배포 환경 | 로컬 (옵션: Heroku) | 로컬 서버로 시작, 필요시 간단한 클라우드 배포 실습 | JAR 실행, 간단 배포 파이프라인 |

- **학습 포인트:** Spring Boot와 JPA의 연동 흐름 이해, 대체 기술(React, JWT 등)을 언급하여 확장 가능성 탐색.

## 3. 개발 환경 구축
### 3.1 데이터베이스 설치 및 설정
- **개발/테스트: H2 데이터베이스**
  1. H2 공식 사이트에서 버전 2.1.214 이상 다운로드
  2. `bin/h2.bat`(Windows) 또는 `bin/h2.sh`(macOS/Linux) 실행
  3. 웹 콘솔 접속 후 JDBC URL을 `jdbc:h2:~/novel`로 생성, 이후 `jdbc:h2:tcp://localhost/~/novel` (서버 모드)
  4. **트러블슈팅:** 파일 락 발생 시 서버 모드 실행 여부 확인
  5. **학습 포인트:** 임베디드 모드와 서버 모드 차이 이해

- **운영 대비: MySQL (Docker 권장)**
  1. Docker 설치 후 아래 명령 실행
     ```bash
     docker run --name mysql-novel -e MYSQL_ROOT_PASSWORD=test1234 -p 3306:3306 -d mysql:8.0
     ```
  2. MySQL Workbench 또는 DBeaver로 접속 테스트
  3. **학습 포인트:** Docker 컨테이너 개념, 환경 변수로 비밀번호 관리

### 3.2 Spring 프로젝트 설정
- Spring Initializr에서 프로젝트 생성 (Dependencies: Spring Web, Spring Data JPA, H2 Database, MySQL Driver, Thymeleaf, Spring Security)
- `application.properties` 기본 예시
  ```properties
  # 공통 JPA 설정
  spring.jpa.hibernate.ddl-auto=update
  spring.jpa.properties.hibernate.format_sql=true
  logging.level.org.hibernate.SQL=debug

  # 개발용 H2
  spring.datasource.url=jdbc:h2:tcp://localhost/~/novel
  spring.datasource.driver-class-name=org.h2.Driver
  spring.datasource.username=sa
  spring.datasource.password=

  # 운영 대비 MySQL (필요 시 주석 해제)
  # spring.datasource.url=jdbc:mysql://localhost:3306/novel_db
  # spring.datasource.username=root
  # spring.datasource.password=test1234

  # 세션 설정
  server.servlet.session.tracking-modes=cookie

  # 파일 업로드
  spring.servlet.multipart.enabled=true
  spring.servlet.multipart.max-file-size=10MB
  spring.servlet.multipart.max-request-size=10MB
  ```
- 프로필 분리(`application-dev.properties`, `application-prod.properties`)를 통해 환경 전환 학습
- **학습 포인트:** Spring Initializr 사용법, 설정 파일로 환경 제어, 프로필 기반 설정 관리

## 4. 핵심 기능 구현 계획
### 4.1 회원 관리 기능
- **도메인:** `Member` 엔티티 (id, email, name, password, joinedAt), `Address` 임베디드 타입
- **리포지토리:** `MemberRepository` (Spring Data JPA 인터페이스)
- **서비스:** `MemberService` (회원 가입, 중복 체크, 로그인 지원 로직)
- **컨트롤러:** 가입/로그인 폼, 가입 처리, 마이페이지
- **학습 포인트:** 엔티티 매핑, 서비스 계층 분리, DTO와 엔티티 구분, 비밀번호 암호화(BCrypt)

### 4.2 소설 콘텐츠 관리 기능
- **도메인:** `Novel` 엔티티 (id, title, authorName, description, coverImagePath, createdAt)
- **리포지토리:** `NovelRepository` (등록/조회/검색)
- **서비스:** `NovelService` (등록/수정/삭제, 이미지 저장 경로 관리)
- **컨트롤러 & 뷰:** 소설 등록 폼, 목록, 상세 페이지
- **이미지 업로드:** `MultipartFile` 사용, `files/novel-covers` 디렉터리에 저장, 파일명 중복 방지를 위한 UUID 적용
- **학습 포인트:** 파일 업로드 처리, 예외 처리, 간단한 도메인 모델 설계

### 4.3 인증 및 유효성 검사
- **인증:** Spring Security의 Form Login 구성, 로그인 성공/실패 처리, 로그인 후 회원 정보 세션 유지
- **인가:** 페이지 접근 제어(예: `/admin/**`), 로그인 필요 페이지 설정
- **검증:** DTO에 `@NotBlank`, `@Email`, `@Size` 등 적용, `BindingResult`를 활용한 오류 메시지 노출
- **학습 포인트:** Security 필터 체인 이해, Bean Validation 실습, 예외 메시지 처리

## 5. 단계별 개발 계획 (8주)
| 단계 | 기간(주) | 주요 과업 | 세부 실행 항목 | 학습 포인트 |
| --- | --- | --- | --- | --- |
| 1단계: 환경 구축 | 1-2주 | 개발 환경 설정 | Git 저장소 생성, Docker DB 구성, 기본 엔티티 생성, Thymeleaf 레이아웃 적용 | 로컬 개발 환경 통일, Git 활용 |
| 2단계: 회원 기능 | 3-4주 | 회원 가입/로그인 | 회원 엔티티/서비스 구현, Security 설정, 가입/로그인 화면 제작, 단위 테스트 작성 | 보안 기초, 테스트 작성 |
| 3단계: 콘텐츠 기능 | 5-6주 | 소설 CRUD 및 이미지 업로드 | 소설 엔티티/서비스 구현, 파일 업로드 처리, 목록/상세/수정 화면 구성, 검증 로직 추가 | CRUD 패턴, 파일 처리 |
| 4단계: 테스트 & 배포 | 7-8주 | 품질 향상 | 통합 테스트, 간단 리팩토링, 로컬 배포 스크립트 작성(JAR 실행), 선택적 Heroku 배포 | 테스트 자동화, 배포 흐름 경험 |

- 매주 스탠드업 및 주간 리뷰를 통해 진행 상황 공유
- 기본 Git 전략: 기능 브랜치 생성 → Pull Request 리뷰 → main 병합
- **학습 포인트:** 애자일 방식 소개, 코드 리뷰 경험, 협업 도구 활용

## 6. 추가 학습 및 확장 아이디어
- 에피소드(회차) 관리, 태그/장르 분류, 검색 기능 등은 후속 프로젝트로 확장
- OAuth2 소셜 로그인, AWS 배포, React 프론트엔드 연동 등 심화 학습 주제로 제안
- 학습 노트/회고 작성으로 지식 정리, 포트폴리오 자료로 활용

---
본 기획서는 학원생이 무리 없이 따라갈 수 있는 범위를 중심으로 구성되었습니다. 상황에 맞게 기능 범위를 조절하고, 팀 프로젝트 시 주간 회의와 코드 리뷰를 적극 활용하시기 바랍니다. 추가 질문이나 개선 요청은 언제든지 환영합니다.

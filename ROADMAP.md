# Spring Boot JPA Novel Serialization Site — Roadmap

## 0. Vision & Product Goal
- **Product vision:** 안정적인 연재 경험과 작가·독자 커뮤니티를 제공하는 프로덕션급 소설 플랫폼 구축
- **2025 핵심 지표(KPI 가이드라인):**
  - 주간 활성 작가 수 200명 → 500명 (연말)
  - 평균 페이지 응답 시간 1초 이하, 오류율 0.1% 이하 유지
  - 스토리 완료율(연재 지속률) 60% 이상

## 1. Current Status (2024 Q4)
- Spring Boot 3.5, Java 21 기반 애플리케이션 골격과 최소 회원 엔티티/저장소 구현
- Spring Security 폼 로그인 기본 구성과 BCrypt 비밀번호 인코딩 설정 완료
- JPA 저장소 단위 테스트 및 애플리케이션 컨텍스트 로딩 테스트 보유
- CI/CD, 검색, 도메인 기능, 운영 인프라 등은 아직 미구현 단계

## 2. Sprint-based Execution Plan (2025)
| Sprint | 기간(예상) | 목표 | 주요 작업 | 완료 기준 |
| --- | --- | --- | --- | --- |
| **Sprint 0** | 1주 | 개발 기반 세팅 | Gradle/Querydsl APT/코드 스타일/공통 오류 처리 뼈대 | `main` 브랜치에 빌드·테스트 성공, 기본 코드 스타일 체크 통과 |
| **Sprint 0.5** | 0.5주 | 관측성·보안 기본 | Spring Boot Actuator, OpenTelemetry SDK, 구조화 로그, 보안 헤더 | 헬스체크/레디니스 엔드포인트 노출, 보안 스캐너 통과 |
| **Sprint 1** | 1주 | 회원·인증 MVP | Member 도메인 확장, 가입/로그인/역할 전환, BCrypt 저장, 인증 테스트 | REST/SSR 회원 기능 E2E, Security 통합 테스트 통과 |
| **Sprint 2** | 2주 | 소설/에피소드 CRUD | Novel/Episode 애그리게이트, S3 업로드 연동, 발행/임시저장, DTO·검증 | 작가가 회차 발행·수정, 통합 테스트 + S3 스텁 테스트 완료 |
| **Sprint 3** | 2주 | 조회·검색 고도화 | Querydsl 동적 쿼리, Keyset 페이지네이션, 인기/장르 필터, 캐시 도입 | 목록 API 성능 지표 충족(조회 20ms 이하), 캐시 히트율 70% |
| **Sprint 4** | 2주 | 커뮤니티·상호작용 | 댓글/대댓글, 즐겨찾기, 노출 정책, 권한 검증 | 댓글/즐겨찾기 API/SSR 테스트, 권한 위반 테스트 케이스 추가 |
| **Sprint 5** | 1주 | 배포·운영 자동화 | Docker 이미지, GitHub Actions CI/CD, AWS(ECS/EC2 + RDS), 모니터링 | 스테이징 자동 배포, Blue/Green 리허설, 관측 지표 대시보드 구축 |

## 3. Milestone Outlook by Quarter
- **2025 Q1:** Sprint 0~1 완료, 핵심 인증/회원 흐름 확보, 기본 관측성 체계 마련
- **2025 Q2:** Sprint 2~3 실행, 소설/회차 도메인 완성과 검색 성능 확보, Redis 캐시 파일럿
- **2025 Q3:** Sprint 4 집중, 커뮤니티 기능과 권한 관리 고도화, 사용자 참여 지표 측정
- **2025 Q4:** Sprint 5 및 후속 안정화, CI/CD + AWS 운영 체계 정착, 비용/성능 최적화 착수

## 4. Long-term Backlog (2026+)
1. **상업화:** 유료 회차·포인트·결제/정산 자동화
2. **추천 시스템:** 규칙 기반 → ML 추천, 랭킹 배치 파이프라인 고도화
3. **확장 채널:** 모바일 앱, API 게이트웨이, 글로벌 결제/다국어 지원
4. **AI 도구:** 작품 요약, 태그 생성, 작가 보조 도구

## 5. Risk & Mitigation
- **트래픽 급증:** 캐시/Keyset 페이지네이션, Auto Scaling, CDN 도입 계획
- **데이터 정합성:** Flyway 마이그레이션, 잠금/낙관적 락(버전 필드) 적용, 주기적 백업
- **보안/저작권:** 콘텐츠 신고 프로세스, 감사 로그, OAuth2/JWT 확장, PII 마스킹
- **인력·지식 편차:** 문서화/코드 리뷰 강화, 도메인/인프라 런북 작성

## 6. Collaboration & Governance
- GitHub Issues/Projects로 Sprint 백로그 관리, Notion으로 설계 문서 버전 관리
- 주간 스탠드업, Sprint Review/Retro, 분기별 OKR 점검
- 품질 게이트: 빌드 성공, 테스트 통과, 커버리지/정적 분석 기준 충족 시 머지 가능

로드맵은 스프린트 회고와 KPI 추이에 따라 분기 단위로 재검토·갱신합니다.

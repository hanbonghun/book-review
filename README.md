# Book Review Service

## 프로젝트 소개
독서 경험을 공유하고 도서 리뷰를 작성/관리할 수 있는 웹 서비스입니다.
- 도서 검색 API를 활용한 통합 도서 검색
- 도서 리뷰 작성 및 공유

## Tech Stack
| Category | Technologies |
|----------|-------------|
| Backend | Java 17, Spring Boot 3.4.1, Spring Data JPA |
| Database | MySQL, Redis |
| Security | Spring Security, OAuth 2.0, JWT |
| Documentation | Spring REST Docs, OpenAPI 3.0 |
| Test | JUnit 5, MockMvc, H2 |

## 주요 기능
- **통합 도서 검색**: 여러 도서 검색 API를 활용한 안정적인 도서 검색 시스템
- **사용자 인증**: OAuth 2.0을 활용한 카카오 소셜 로그인
- **리뷰 시스템**: 별점, 리뷰 작성 및 공유 기능

## API 문서
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- REST Docs: `http://localhost:8080/docs/index.html`

## 실행 방법
```bash
# 프로젝트 클론
git clone [repository-url]

# 환경 변수 설정
cp .env.example .env
# .env 파일 수정

# 애플리케이션 실행
./gradlew bootRun
```

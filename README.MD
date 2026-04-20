Smart Factory Predictive Maintenance Backend
이 프로젝트는 공장 내 AGV 및 OHT의 장비 상태를 모니터링하는 백엔드 서버입니다.

1. 주요 기능
실시간 센서 데이터 모니터링: WebSocket(STOMP)과 SockJS를 사용하여 장비별 센서 데이터를 실시간으로 클라이언트에 전송합니다.

센서 데이터 분석 및 자동 알림: 스케줄러가 20초마다 최신 데이터를 검사하여 임계값 규칙(ThresholdRule) 초과 시 알림(AlertEvent)과 정비 오더(MaintOrder)를 자동으로 생성합니다.

장비 및 공정 라인 가동 현황: 공장(Fab) 및 라인별 장비의 가동, 정지, 정비 상태를 요약하여 제공하며 가동률을 관리합니다.

데이터 마이그레이션: 로컬의 JSON 라벨링 데이터를 Aiven 클라우드 데이터베이스로 일괄 적재할 수 있는 Python 기반 마이그레이션 도구를 포함합니다.

보안 및 인증: JWT(JSON Web Token)와 Spring Security를 사용하여 Stateless한 인증 시스템을 구축하고 접근 권한을 제어합니다.

2. 기술 스택
Framework: Spring Boot 3.2.5, Spring Data JPA

Language: Java 17

Database: MySQL (Aiven Cloud DB), Hibernate

Security: Spring Security, JWT

Messaging: Spring WebSocket (STOMP)

API Docs: SpringDoc OpenAPI (Swagger)

Build: Gradle

Migration: Python 3.x, pymysql

3. 설치 및 실행 방법
환경 변수 설정
실행 시 다음 환경 변수가 필요합니다.

DB_PASSWORD: 데이터베이스 접속 비밀번호

DB_URL: (선택) 데이터베이스 접속 URL (기본값: Aiven Cloud MySQL)

DB_USERNAME: (선택) 데이터베이스 사용자 아이디 (기본값: avnadmin)

Docker 실행
Bash
# 빌드 및 이미지 생성
./gradlew clean build -x test
docker build -t predictive-server .

# 컨테이너 실행
docker run -d -p 8080:8080 -e DB_PASSWORD=비밀번호 predictive-server
4. 주요 API 엔드포인트
인증: POST /api/auth/login

대시보드:

GET /api/dashboard/summary: 가동/정지/정비 장비 수 요약

GET /api/dashboard/line-status: 라인별 세부 가동 현황

장비 관리:

GET /api/equipments: 장비 목록 조회 (필터링 포함)

GET /api/equipments/{id}/sensors/history: 최신 60건 센서 이력 조회

알림 및 오더:

GET /api/alerts: 알림 목록 조회

PATCH /api/alerts/{alertId}/ack: 알림 확인 처리

Swagger UI: /swagger-ui/index.html

5. 데이터 마이그레이션
라벨링데이터 폴더에 마이그레이션할 JSON 파일들을 배치합니다.

migration.py에서 db_config 정보를 확인합니다.

필수 라이브러리 설치 후 스크립트를 실행합니다: python migration.py.

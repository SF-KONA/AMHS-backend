# 1. 자바 17 환경의 가벼운 리눅스 이미지를 베이스로 사용
FROM eclipse-temurin:17-jdk-alpine

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. 방금 빌드해서 만든 JAR 파일을 컨테이너 내부로 복사
COPY build/libs/*.jar app.jar

# 4. 서버 포트 8080 열어주기
EXPOSE 8080

# 5. 서버 실행 명령어
ENTRYPOINT ["java", "-jar", "app.jar"]
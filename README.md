# Lotus AWS AI 프로젝트

Spring Boot 3.2.3과 AWS AI 서비스(Bedrock, SageMaker)를 연동한 AI 애플리케이션입니다.

## 🚀 주요 기능

- **AWS Bedrock 연동**: Claude 3 Sonnet, Claude 3 Haiku, Titan Text 모델 지원
- **RESTful API**: AI 모델 호출을 위한 REST API 제공
- **Spring Boot 3.2.3**: 최신 Spring Boot 프레임워크 사용
- **Gradle 빌드**: Gradle을 통한 의존성 관리 및 빌드
- **Actuator**: 애플리케이션 모니터링 및 헬스체크
- **Comprehensive Testing**: 단위 테스트 및 통합 테스트 포함

## 📋 요구사항

- Java 17 이상
- Gradle 8.5
- AWS 계정 및 자격 증명
- AWS Bedrock 서비스 접근 권한

## 🛠️ 설치 및 실행

### 1. 프로젝트 클론
```bash
git clone <repository-url>
cd lotus-awsai
```

### 2. AWS 자격 증명 설정
환경 변수로 AWS 자격 증명을 설정합니다:

```bash
export AWS_ACCESS_KEY_ID=your_access_key_id
export AWS_SECRET_ACCESS_KEY=your_secret_access_key
export AWS_REGION=us-east-1
```

### 3. 프로젝트 빌드
```bash
./gradlew clean build
```

### 4. 애플리케이션 실행
```bash
./gradlew bootRun
```

애플리케이션이 `http://localhost:8080`에서 실행됩니다.

## 📚 API 사용법

### 1. 헬스체크
```bash
curl http://localhost:8080/api/ai/health
```

### 2. Claude 3 Sonnet 모델 호출
```bash
curl -X POST http://localhost:8080/api/ai/claude \
  -H "Content-Type: application/json" \
  -d '{"prompt": "Hello, how are you?"}'
```

### 3. Titan Text 모델 호출
```bash
curl -X POST http://localhost:8080/api/ai/titan \
  -H "Content-Type: application/json" \
  -d '{"prompt": "Hello, how are you?"}'
```

## 🏗️ 프로젝트 구조

```
lotus-awsai/
├── src/
│   ├── main/
│   │   ├── java/com/lotus/lotusawsai/
│   │   │   ├── config/
│   │   │   │   ├── AwsConfig.java
│   │   │   │   └── JacksonConfig.java
│   │   │   ├── controller/
│   │   │   │   └── AiController.java
│   │   │   ├── service/
│   │   │   │   └── AiService.java
│   │   │   └── LotusAwsaiApplication.java
│   │   └── resources/
│   │       └── application.yml
│   └── test/
│       └── java/com/lotus/lotusawsai/
│           ├── controller/
│           │   └── AiControllerTest.java
│           └── service/
│               └── AiServiceTest.java
├── build.gradle
├── settings.gradle
├── gradlew
├── .cursorrules
└── README.md
```

## 🔧 설정

### application.yml 주요 설정

```yaml
server:
  port: 8080

spring:
  application:
    name: lotus-awsai

aws:
  region: us-east-1
  credentials:
    access-key-id: ${AWS_ACCESS_KEY_ID}
    secret-access-key: ${AWS_SECRET_ACCESS_KEY}
  
  bedrock:
    region: us-east-1
    models:
      claude-3-sonnet: anthropic.claude-3-sonnet-20240229-v1-0
      claude-3-haiku: anthropic.claude-3-haiku-20240307-v1-0
      titan-text: amazon.titan-text-express-v1

management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics
```

## 🧪 테스트

### 단위 테스트 실행
```bash
./gradlew test
```

### 특정 테스트 클래스 실행
```bash
./gradlew test --tests AiServiceTest
./gradlew test --tests AiControllerTest
```

## 📊 모니터링

### Actuator 엔드포인트
- 헬스체크: `http://localhost:8080/actuator/health`
- 애플리케이션 정보: `http://localhost:8080/actuator/info`
- 메트릭: `http://localhost:8080/actuator/metrics`

## 🔒 보안 고려사항

### AWS 자격 증명 관리
- 환경 변수 사용 권장
- IAM 역할 활용 (EC2, ECS 등에서)
- 최소 권한 원칙 적용

### API 보안
- 입력 검증 구현
- CORS 설정 고려
- Rate Limiting 적용 권장

## 🚀 배포

### JAR 파일 생성
```bash
./gradlew bootJar
```

### JAR 파일 실행
```bash
java -jar build/libs/lotus-awsai-0.0.1-SNAPSHOT.jar
```

### Docker 배포 (선택사항)
```dockerfile
FROM openjdk:17-jre-slim
COPY build/libs/lotus-awsai-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

## 🐛 문제 해결

### 일반적인 문제들

1. **AWS 자격 증명 오류**
   - 환경 변수 확인: `echo $AWS_ACCESS_KEY_ID`
   - AWS CLI 설정 확인: `aws configure list`

2. **모델 ID 오류**
   - application.yml의 모델 ID 확인
   - AWS Bedrock 서비스 접근 권한 확인

3. **네트워크 오류**
   - AWS 리전 설정 확인
   - VPC 및 보안 그룹 설정 확인

4. **메모리 부족**
   - JVM 힙 크기 조정: `-Xmx2g -Xms1g`

### 디버깅 명령어
```bash
# 애플리케이션 로그 확인
./gradlew bootRun --debug

# 의존성 확인
./gradlew dependencies

# 빌드 정보 확인
./gradlew build --info
```

## 🤝 기여하기

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 라이선스

이 프로젝트는 MIT 라이선스 하에 배포됩니다.

## 📞 지원

문제가 발생하거나 질문이 있으시면 이슈를 생성해 주세요.

---

**Lotus AWS AI 프로젝트** - Spring Boot와 AWS AI 서비스의 강력한 조합
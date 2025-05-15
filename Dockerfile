# 빌드 스테이지
FROM eclipse-temurin:21 AS builder

WORKDIR /app

COPY . .

RUN ./gradlew clean build -x test

# 런 스테이지
FROM eclipse-temurin:21

WORKDIR /app

COPY .env .env
COPY --from=builder /app/build/libs/*-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
# Build stage
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# 最終安定化設定
# ポート指定はプロパティファイル (${PORT:8080}) で解決されるため
# 起動コマンドはシンプルに保ち、余計な変数を排除します。
ENTRYPOINT ["java", \
    "-XX:MaxRAMPercentage=75.0", \
    "-XX:+UseSerialGC", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-Dspring.profiles.active=prod", \
    "-jar", "app.jar"]

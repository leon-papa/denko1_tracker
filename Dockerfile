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

# 起動時間短縮のための最適化設定
# -XX:MaxRAMPercentage: コンテナメモリの75%を使用
# -XX:+UseSerialGC: 低スペック環境向けの軽量GC
# -Djava.security.egd: 乱数生成の高速化（起動時間の短縮）
ENTRYPOINT ["java", \
    "-XX:MaxRAMPercentage=75.0", \
    "-XX:+UseSerialGC", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-Dspring.profiles.active=prod", \
    "-Dserver.port=${PORT}", \
    "-jar", "app.jar"]

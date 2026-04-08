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

# Renderのメモリ制限(512MB)に合わせつつ、ポートを確実に指定
# MaxRAMPercentage=75.0 (約380MB) をJavaに割り当て、残りをOSに
ENTRYPOINT ["java", \
    "-XX:MaxRAMPercentage=75.0", \
    "-XX:+UseSerialGC", \
    "-Dspring.profiles.active=prod", \
    "-Dserver.port=${PORT}", \
    "-jar", "app.jar"]

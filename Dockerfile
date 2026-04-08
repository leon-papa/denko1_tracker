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

# JVMフラグは -jar の前に置く。メモリ制限でRenderフリープラン(512MB)のOOMを防ぐ
ENTRYPOINT ["java", \
  "-Xmx256m", \
  "-Xss256k", \
  "-XX:MaxMetaspaceSize=192m", \
  "-XX:+UseSerialGC", \
  "-Dspring.profiles.active=prod", \
  "-jar", "app.jar"]

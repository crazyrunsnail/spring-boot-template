FROM openjdk:17-jdk-slim-buster AS builder

COPY pom.xml mvnw ./
COPY .mvn .mvn
RUN ./mvnw dependency:resolve

COPY src src
RUN ./mvnw -DskipTests package

FROM eclipse-temurin:17.0.12_7-jre-jammy
WORKDIR app
COPY --from=builder target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
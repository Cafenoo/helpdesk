FROM maven:3.8.1-openjdk-11-slim AS build
WORKDIR /workspace
COPY pom.xml .
COPY src /workspace/src
RUN mvn clean package -Dmaven.test.skip

FROM adoptopenjdk/openjdk11:x86_64-alpine-jdk-11.0.4_11 AS final
COPY --from=build /workspace/target/helpdesk.jar helpdesk.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "helpdesk.jar"]

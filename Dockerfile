FROM docker.io/library/eclipse-temurin:17-jdk
LABEL authors="SDLY"
WORKDIR /app
COPY target/Wwork4-0.0.1-SNAPSHOT.jar app.jar
ENV TZ=Asia/Shanghai
ENTRYPOINT ["java", "-jar", "app.jar"]
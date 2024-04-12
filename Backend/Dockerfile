FROM eclipse-temurin:17-jre as builder
WORKDIR application
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} application.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","application.jar"]
FROM eclipse-temurin:17-jre as builder
WORKDIR application
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} application.jar
ENV AZURE_POSTGRESQL_URL=${AZURE_POSTGRESQL_URL}
ENV AZURE_POSTGRESQL_USERNAME=${AZURE_POSTGRESQL_USERNAME}
ENV AZURE_POSTGRESQL_PASSWORD=${AZURE_POSTGRESQL_PASSWORD}
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","application.jar"]
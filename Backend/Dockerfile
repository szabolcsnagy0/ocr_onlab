FROM eclipse-temurin:17-jre as builder
WORKDIR application
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} application.jar
ENV AZURE_POSTGRESQL_URL=${AZURE_POSTGRESQL_URL}
ENV AZURE_POSTGRESQL_USERNAME=${AZURE_POSTGRESQL_USERNAME}
ENV AZURE_POSTGRESQL_PASSWORD=${AZURE_POSTGRESQL_PASSWORD}
ENV JWT_KEY=${JWT_KEY}
# Install Python
RUN apt-get update && \
    apt-get install -y python3 python3-pip
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","application.jar"]
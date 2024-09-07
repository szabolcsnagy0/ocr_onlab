FROM eclipse-temurin:17-jre as builder
WORKDIR application
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} application.jar
COPY ocr ./ocr
ENV AZURE_POSTGRESQL_URL=${AZURE_POSTGRESQL_URL}
ENV AZURE_POSTGRESQL_USERNAME=${AZURE_POSTGRESQL_USERNAME}
ENV AZURE_POSTGRESQL_PASSWORD=${AZURE_POSTGRESQL_PASSWORD}
ENV JWT_KEY=${JWT_KEY}
# Install Python and pip
RUN apt-get update && \
    apt-get install -y python3 python3-pip libgl1 libglx-mesa0 libglib2.0-0 libsm6 libxrender1 libxext6

    # Install OpenCV, Numpy, PaddlePaddle and PaddleOCR
RUN pip3 install opencv-python-headless numpy paddlepaddle paddleocr

ENV PATH="$PATH:/usr/bin/python3"
ENV PYTHONPATH="$PYTHONPATH:/usr/lib/python3/dist-packages"
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","application.jar"]

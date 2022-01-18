#
# Build stage
#
FROM maven:3.6.0-jdk-11-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

#
# Package stage
#
FROM openjdk:11-jre-slim
COPY --from=build /home/app/target/bifanas-backend.jar /usr/local/lib/app.jar

ENV DEBIAN_FRONTEND=noninteractive
RUN apt update && apt install -y tesseract-ocr=4.1.1-2.1 && rm -rf /var/lib/apt/lists/*

# cd /usr/local/lib
WORKDIR /usr/local/lib

# copy tessdata for tesseract
RUN mkdir tessdata
COPY tessdata/por.traineddata tessdata/por.traineddata

# create a temporary folder for file storage
RUN mkdir public

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/usr/local/lib/app.jar"]

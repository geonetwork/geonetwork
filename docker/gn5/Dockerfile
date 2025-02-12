FROM openjdk:21-bookworm

RUN apt update && apt install -y gdal-bin

RUN groupadd --system spring && useradd -g spring  --system spring

USER spring:spring

ARG JAR_FILE=./*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

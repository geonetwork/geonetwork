FROM eclipse-temurin:21-noble


RUN apt update && apt install -y gdal-bin

RUN groupadd --system -g 999 spring && \
    useradd -g spring -u 999 --system spring

USER spring:spring

ARG JAR_FILE=./*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

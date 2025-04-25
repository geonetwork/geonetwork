FROM eclipse-temurin:21-noble


RUN apt update && apt install -y gdal-bin

USER www-data:www-data

ARG JAR_FILE=./*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

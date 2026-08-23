# Stage 1: Build the application
FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app
COPY . .
RUN ./mvnw clean install -DskipTests -Drelax && \
    find src/apps/geonetwork/target/ -maxdepth 1 -name "gn-main-app-*.jar" ! -name "*-sources.jar" ! -name "*-javadoc.jar" ! -name "*.original" -exec cp {} app.jar \;

# Stage 2: Run the application
FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /app/app.jar app.jar

# Copy configuration directory if needed by the app
COPY --from=build /app/config ./config

EXPOSE 7979

ENTRYPOINT ["java", "-jar", "app.jar"]

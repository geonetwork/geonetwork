# Stage 1: Build the application
FROM maven:3.9.9-eclipse-temurin-21 AS build

WORKDIR /app
COPY . .
RUN ./mvnw clean install -DskipTests -Drelax

# Stage 2: Run the application
FROM eclipse-temurin:21-jre
WORKDIR /app

# The artifact ID for src/apps/geonetwork is gn-main-app
COPY --from=build /app/src/apps/geonetwork/target/gn-main-app-*.jar app.jar

# Copy configuration directory if needed by the app
COPY --from=build /app/config ./config

EXPOSE 7979

ENTRYPOINT ["java", "-jar", "app.jar"]

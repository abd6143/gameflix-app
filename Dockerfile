# Stage 1: Build — use Maven + JDK 17 image to compile the Spring Boot JAR
FROM maven:3.9-eclipse-temurin-17 AS build

# Set working directory inside the build container
WORKDIR /app

# Copy POM first so dependency download layer can be cached separately
COPY pom.xml .

# Pre-download all Maven dependencies for faster rebuilds
RUN mvn dependency:go-offline

# Copy application source code into the build container
COPY src ./src

# Package the app into an executable JAR, skipping tests for Docker speed
RUN mvn package -DskipTests

# Stage 2: Run — use a slim JRE-only image for production runtime
FROM eclipse-temurin:17-jre-alpine

# Set runtime working directory
WORKDIR /app

# Copy the built JAR from the build stage into the runtime image
COPY --from=build /app/target/gameflix-*.jar app.jar

# Expose the default Spring Boot HTTP port
EXPOSE 8080

# Launch the application when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]

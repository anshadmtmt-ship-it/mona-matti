# ==========================================================================
# MONA MATTI — Multi-Stage Production Dockerfile
# Stage 1: Build Stage (Maven + OpenJDK 17)
# Stage 2: Runtime Stage (Lightweight Temurin JRE 17)
# ==========================================================================

# --------------------------------------------------------------------------
# Stage 1: Build Application Package
# --------------------------------------------------------------------------
FROM maven:3.9.6-eclipse-temurin-17-alpine AS builder

WORKDIR /build

# Copy Maven POM and dependency definitions first for efficient layer caching
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy application source code
COPY src ./src

# Package production executable JAR without running tests during image build
RUN mvn clean package -DskipTests

# --------------------------------------------------------------------------
# Stage 2: Production Runtime Image
# --------------------------------------------------------------------------
FROM eclipse-temurin:17-jre-alpine

LABEL maintainer="MONA MATTI <hello@monamatti.com>"
LABEL description="MONA MATTI - Premium Multi-Nib Pencil Application"

# Install curl for container healthchecks
RUN apk add --no-cache curl

WORKDIR /app

# Copy packaged JAR artifact from builder stage
COPY --from=builder /build/target/*.jar /app/app.jar

# Expose default application port
EXPOSE 8099

# Environment variable defaults
ENV SERVER_PORT=8099 \
    SPRING_PROFILES_ACTIVE=prod \
    DB_HOST=localhost \
    DB_PORT=3306 \
    DB_NAME=mona_matti \
    DB_USERNAME=root \
    DB_PASSWORD=root

# Container Healthcheck using Spring Boot Actuator
HEALTHCHECK --interval=15s --timeout=5s --start-period=30s --retries=3 \
  CMD curl -f http://localhost:${SERVER_PORT}/actuator/health || exit 1

# Execute application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

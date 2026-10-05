# ============================================
# Multi-stage Dockerfile for LMS Application
# ============================================

# --- Stage 1: Build with Maven ---
FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

# Copy pom.xml first for dependency caching
COPY pom.xml .

RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build application
RUN mvn package -DskipTests -B


# --- Stage 2: Runtime with JRE ---
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Create non-root user
RUN addgroup -S appgroup && \
    adduser -S appuser -G appgroup

# Create directories required by application
RUN mkdir -p /app /logs && \
    chown -R appuser:appgroup /app /logs

# Copy built JAR
COPY --from=build /app/target/*.jar /app/app.jar

# Make sure appuser owns application files
RUN chown -R appuser:appgroup /app

# Run as non-root user
USER appuser

# Application port
EXPOSE 9090

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD wget -qO- http://localhost:9090/actuator/health || exit 1

# Start application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
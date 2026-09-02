# ============================================
# Multi-stage Dockerfile for LMS Application
# ============================================
# Stage 1: Build
# Stage 2: Runtime
# ============================================

# --- Stage 1: Build with Maven ---
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copy pom.xml first for dependency caching
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build
COPY src ./src
RUN mvn package -DskipTests -B

# --- Stage 2: Runtime with JRE ---
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Create non-root user for security
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Copy built JAR from build stage
COPY --from=build /app/target/*.jar app.jar

# Switch to non-root user
USER appuser

# Expose application port
EXPOSE 9090

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD wget -qO- http://localhost:9090/actuator/health || exit 1

# Run the application
# Profile is set via SPRING_PROFILES_ACTIVE environment variable
ENTRYPOINT ["java", "-jar", "app.jar"]

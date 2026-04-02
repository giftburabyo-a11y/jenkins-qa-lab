# ─────────────────────────────────────────────────────────────────────
# Maven + JDK 17 base image
# ─────────────────────────────────────────────────────────────────────
FROM maven:3.9.6-eclipse-temurin-17

WORKDIR /app

# Copy pom.xml first to cache dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B --no-transfer-progress

# Copy test source code
COPY src ./src

# Default: run all tests
CMD ["mvn", "test", "-B", "--no-transfer-progress"]
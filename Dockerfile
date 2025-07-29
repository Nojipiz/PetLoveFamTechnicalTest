# Use OpenJDK 17 with SBT for Scala 3.3.6
FROM sbtscala/scala-sbt:eclipse-temurin-alpine-24.0.1_9_1.11.2_3.3.6

# Install curl for health checks
RUN apk add --no-cache curl

# Set working directory
WORKDIR /app

# Copy build files first (for better Docker layer caching)
COPY build.sbt .
COPY project/ project/

# Download dependencies (this layer will be cached if build.sbt doesn't change)
RUN sbt update

# Copy source code
COPY src/ src/
COPY project/ project/
COPY .scalafmt.conf .
COPY src/main/graphql/petlovefam.graphql .

# Expose port 8080
EXPOSE 8080

# Set environment variables
ENV JAVA_OPTS="-Xmx512m -Xms256m"
ENV SBT_OPTS="-Xmx512m -Xms256m"

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/graphiql || exit 1

# Start the application
CMD ["sbt", "run"]

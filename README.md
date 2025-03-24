# Ratings Service

A Spring Boot microservice that provides movie ratings functionality, offering both batch and individual rating lookups. This service is designed to work in conjunction with the Movies service, providing comprehensive rating information for movies.

## Features

- RESTful APIs for movie ratings management
- Batch rating lookups for multiple movies
- Individual movie rating queries
- SQLite database for data persistence
- OpenAPI/Swagger documentation
- Actuator endpoints for health monitoring and metrics
- OpenTelemetry integration for distributed tracing
- JaCoCo code coverage reporting
- Checkstyle for code quality
- Docker support

## Prerequisites

- Java 17 or higher
- Gradle 8.x
- Docker (optional, for containerization)

## Quick Start

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd ratings
   ```

2. Add execution permission to Gradle wrapper:
   ```bash
   chmod +x gradlew
   ```

3. Build and run tests:
   ```bash
   ./gradlew clean build
   ```

4. Generate test coverage report:
   ```bash
   ./gradlew test jacocoTestReport
   ```

5. Start the application:
   ```bash
   ./gradlew bootRun
   ```

The service will start on port 8081.

## API Documentation

The API documentation is available through Swagger UI when the application is running:
- Swagger UI: http://localhost:8081/swagger-ui/index.html
- OpenAPI Spec: http://localhost:8081/v3/api-docs.yaml

### Key Endpoints

- `POST /api/v1/ratings/movies`: Get ratings for multiple movies (batch lookup)
- `GET /api/v1/ratings/movie/{movieId}`: Get rating for a specific movie

### Response Format

The service returns rating information in the following format:
```json
{
    "movieId": 1,
    "averageRating": 8.9,
    "numberOfRatings": 2500
}
```

## Monitoring and Metrics

The application exposes various actuator endpoints for monitoring:

- Health Check: http://localhost:8081/actuator/health
- Info: http://localhost:8081/actuator/info
- Metrics (Prometheus): http://localhost:8081/actuator/prometheus

## Database

The application uses SQLite as its database. The database file is included in the repository at `src/main/resources/ratings.db`.

Key features:
- JPA/Hibernate for data access
- Automatic schema updates
- SQL query logging for debugging
- Optimized queries for batch operations

## Configuration

The application can be configured through `application.yaml`. Key configurations include:

- Server port and address
- Database connection settings
- Actuator endpoint exposure
- Logging configuration
- SQL query formatting and display

Environment-specific configurations are available in separate YAML files.

## Distributed Tracing

OpenTelemetry integration is available for distributed tracing. Enable it using the OpenTelemetry Java agent and environment variables.

## Code Quality

- JaCoCo for code coverage reporting
- Checkstyle for code style enforcement
- Unit tests for core functionality

## Docker Support

Build the Docker image:
```bash
docker build -t ratings-service .
```

Run using Docker Compose:
```bash
docker-compose up
```

## Integration with Movies Service

This service is designed to work with the Movies service:
- Movies service runs on port 8080
- Ratings service runs on port 8081
- Movies service calls this service to fetch rating information

## Future Improvements

1. API Gateway integration for:
   - Authentication/Authorization
   - Rate limiting
   - Request routing
   - Distributed Caching

2. Additional test coverage:
   - Performance tests
   - API contract tests


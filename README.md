OpenApi UI endpoints
http://localhost:8081/swagger-ui/index.html

Application Health endpoint
http://localhost:8081/actuator/health

Application metrics endpoint (Prometheus)
http://localhost:8081/actuator/prometheus

- Tracing can be enabled levaraging open telemetry agent and environment variables

- Common logging format can be reffered in logback-spring.xml

- Separate applications yaml files for each environment

- Created index for movieId in ratings DB

docker network create mynetwork

Created some tests (not complete due to time constraints)
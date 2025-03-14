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

- All the requested apis were tested using Postman. Unit tests are not completely done due to time constraints.

- Docker image can be built using Docker file

- api spec is available in the project root api-spec.yaml. This spec can also be downloaded from running app http://localhost:8080/v3/api-docs.yaml
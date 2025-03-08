# Project Instructions for GitHub Copilot

## Project Overview
This is a Java-based monorepo containing multiple microservices. Each service follows clean architecture principles with a clear separation of commands and queries (CQRS). The project adopts API-first design using OpenAPI specifications and employs event-driven architecture with Kafka. The project aims to be maintainable, testable, and scalable.

## GitHub Copilot Guidelines
### Code Generation Best Practices
- Ensure Copilot-generated code adheres to project coding standards.
- Always review, test, and refactor suggestions before committing.
- Use inline comments to guide Copilot when generating complex logic.

## Technology Stack
- Java 21 (LTS)
- Quarkus framework
- RESTful API design with OpenAPI/Swagger
- Apache Kafka for event streaming
- Maven for dependency management
- JUnit 5 for testing
- PostgreSQL for database

## Monorepo Structure
- Organized with multiple services in the apps folder
- Shared libraries and utilities in the libs folder
- Common configuration in the config folder

## API-First Design
- Define APIs using OpenAPI 3.1 specifications before implementation
- Store API specifications in dedicated `api` folder for each service
- Generate server stubs and client SDKs from OpenAPI specifications
- Implement interfaces generated from OpenAPI specs
- Validate requests against OpenAPI schemas
- Define API versioning (`v1`, `v2`, etc.) and establish a deprecation policy.

## Coding Standards
- Follow Google Java Style Guide
- Use meaningful variable and method names
- Include Javadoc for all public methods and classes
- Implement proper error handling
- Use design patterns where appropriate
- Keep methods small and focused on a single responsibility

## Architecture
- Follow clean architecture principles
- Implement Command Query Responsibility Segregation (CQRS)
- Separate concerns between layers:
  - API/Controller (External interfaces defined by OpenAPI specs)
  - Application (Use cases implementing commands and queries)
  - Domain (Business logic and entities)
  - Infrastructure (External services, databases, Kafka producers/consumers)

## Command and Query Separation
- Commands: Operations that change state and don't return data
- Queries: Operations that return data but don't change state
- Each use case should be a dedicated command or query class, all organized within the usecases directory
- Follow naming convention: [Action][Entity][Command/Query] (e.g., CreateUserCommand, GetUserByIdQuery)

## Testing Guidelines
- Organize tests by:
  - Use Cases: Testing application logic in isolation
  - Adapters: Testing integration with external boundaries
- Write unit tests for all use cases (both commands and queries)
- Implement integration tests for repositories and external services
- Create API tests validating against OpenAPI contracts
- Test Kafka producers and consumers using test containers
- Aim for at least 80% code coverage

## Security Considerations
- Implement proper input validation
- Use parameterized queries to prevent SQL injection
- Apply authentication and authorization where necessary
- Protect sensitive data
- Implement API security as defined in OpenAPI specs (OAuth2, API keys, etc.)

## Performance Guidelines
- Optimize database queries
- Implement caching where appropriate
- Consider asynchronous processing for long-running tasks
- Profile and optimize critical code paths
- Optimize Kafka topic partitioning for parallel processing

## Dependency Management & Tooling
- Use Maven BOM (Bill of Materials) to ensure consistent dependency versions.
- Favor library versions that align with LTS support.
- Scan dependencies for vulnerabilities (e.g., OWASP Dependency Check).
- Ensure builds pass with `mvn verify` before pushing changes.

## Observability Stack
- Implement the three pillars of observability: logs, metrics, and traces
- Use OpenTelemetry as the vendor-agnostic observability framework
- Export telemetry data to your preferred backends (Jaeger, Prometheus, Elasticsearch, etc.)
- Follow W3C trace context specification for consistent cross-service tracing

## Logging
- Use structured logging with JSON format (Logback + SLF4J)
- Set appropriate log levels (`DEBUG`, `INFO`, `WARN`, `ERROR`)
- Include trace and span IDs in log entries for correlation
- Centralize logs in a searchable platform (e.g., Elasticsearch)

## Tracing & Metrics with OpenTelemetry
- Implement distributed tracing across all services
- Configure OpenTelemetry SDK in each service
- Automatically instrument frameworks and libraries where possible
- Add custom instrumentation for business-critical operations
- Create custom spans for important business transactions
- Define and collect custom metrics for business KPIs
- Expose Prometheus endpoint in each service
- Track the four golden signals: latency, traffic, errors, and saturation
- Set up dashboards for visualizing metrics and traces

## Error Handling & Resilience
- Define a global exception handling strategy
- Use retry mechanisms for transient failures
- Implement graceful degradation (e.g., circuit breakers with Resilience4j)
- Set up alerts based on error rates and SLOs

## Example Service Structure

```
apps/
├── service-a/
│   ├── api/
│   │   └── openapi.yaml
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/example/servicea/
│   │   │   │       ├── api/
│   │   │   │       │   ├── rest/
│   │   │   │       │   └── grpc/
│   │   │   │       ├── application/
│   │   │   │       │   └── usecases/
│   │   │   │       │       ├── CreateUserCommand.java
│   │   │   │       │       ├── UpdateUserCommand.java
│   │   │   │       │       ├── GetUserByIdQuery.java
│   │   │   │       │       └── ListUsersQuery.java
│   │   │   │       ├── domain/
│   │   │   │       │   ├── model/
│   │   │   │       │   ├── events/
│   │   │   │       │   └── services/
│   │   │   │       ├── infrastructure/
│   │   │   │       │   ├── config/
│   │   │   │       │   │   └── OpenTelemetryConfig.java
│   │   │   │       │   ├── persistence/
│   │   │   │       │   ├── kafka/
│   │   │   │       │   │   ├── producers/
│   │   │   │       │   │   └── consumers/
│   │   │   │       │   ├── external/
│   │   │   │       │   └── observability/
│   │   │   │       │       ├── CustomMetrics.java
│   │   │   │       │       ├── TracingAspect.java
│   │   │   │       │       └── MetricsService.java
│   │   │   └── resources/
│   │   │       ├── application.properties
│   │   │       ├── otel-collector-config.yaml
│   │   │       └── avro/
│   │   │           └── user-events.avsc
│   │   └── test/
│   │       └── java/
│   │           └── com/example/servicea/
│   │               ├── usecases/
│   │               │   ├── CreateUserCommandTest.java
│   │               │   ├── UpdateUserCommandTest.java
│   │               │   ├── GetUserByIdQueryTest.java
│   │               │   └── ListUsersQueryTest.java
│   │               └── adapters/
│   │                   ├── persistence/
│   │                   ├── api/
│   │                   ├── kafka/
│   │                   └── external/
│   └── pom.xml
├── service-b/
│   └── ...
└── service-c/
    └── ...
libs/
├── common/
├── api-clients/
└── shared-model/
config/
├── shared-config/
└── kafka/
    └── topics.yaml
```

## OpenTelemetry Setup for Services

Each service should include the following OpenTelemetry components:

1. **Dependencies in pom.xml**:
   - OpenTelemetry API and SDK
   - Auto-instrumentation agents for:
     - Web frameworks (JAX-RS, Servlet)
     - Kafka clients
     - JDBC/Database drivers
     - HTTP clients

2. **Configuration**:
   - `OpenTelemetryConfig.java` for SDK setup and exporter configuration
   - Environment-specific settings in application properties

3. **Custom Instrumentation**:
   - `TracingAspect.java` for AOP-based method tracing
   - `CustomMetrics.java` for business-specific metrics
   - `MetricsService.java` for centralizing metric collection

4. **OpenTelemetry Collector**:
   - `otel-collector-config.yaml` defining receivers, processors, and exporters
   - Deployment options for sidecar or centralized collector

5. **Integration Points**:
   - Trace context propagation across service boundaries
   - Correlation IDs in logs linked to trace IDs
   - Health checks exposing telemetry status
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
- AsyncAPI for event-driven API specifications
- Maven for dependency management
- JUnit 5 for testing
- PostgreSQL for database

## Monorepo Structure
- Organized with multiple services in the apps folder
- Shared libraries and utilities in the libs folder
- Common configuration in the config folder

## API-First Design
- Define REST APIs using OpenAPI 3.1 specifications before implementation
- Define event-driven APIs using AsyncAPI specifications for Kafka interfaces
- Store API specifications in dedicated folders:
  - `api/rest` for OpenAPI specs
  - `api/events` for AsyncAPI specs
- Generate server stubs, client SDKs, and Kafka producers/consumers from specifications
- Implement interfaces generated from API specs
- Validate requests and messages against schemas
- Define API versioning (`v1`, `v2`, etc.) and establish a deprecation policy

## Coding Standards
- Follow Google Java Style Guide
- Use meaningful variable and method names
- Include Javadoc for all public methods and classes
- Implement proper error handling
- Use design patterns where appropriate
- Keep methods small and focused on a single responsibility
- Use Project Lombok to reduce boilerplate code:
  - Use `@Data` for data classes
  - Use `@Builder` for builder pattern
  - Use `@Slf4j` for logging
  - Use `@RequiredArgsConstructor` for dependency injection
  - Prefer constructor injection with `final` fields
  - Use `@Value` for immutable classes
  - Avoid `@ToString` on entity classes to prevent circular references

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

## Database Migration Strategy
- Use Liquibase for database schema evolution and versioning
- Store changelog files in a dedicated directory structure:
  - `src/main/resources/db/changelog/` for main changelog files
  - Use semantic versioning for changelog files (e.g., `v1.0.0.xml`, `v1.1.0.xml`)
- Organize changes by type and entity:
  - `db/changelog/changes/tables/` - Table creation and modifications
  - `db/changelog/changes/sequences/` - Sequence definitions
  - `db/changelog/changes/data/` - Reference/seed data
  - `db/changelog/changes/constraints/` - Foreign keys and constraints
  - `db/changelog/changes/indexes/` - Index creation
- Follow naming conventions:
  - `YYYYMMDDHHMMSS_short-description.xml` for change files
- Include descriptive comments for each changeset
- Implement rollback procedures for all changes
- Test migrations in CI pipeline using an in-memory database
- Separate DDL (Data Definition Language) from DML (Data Manipulation Language)
- Run migrations automatically during application startup in development
- Use controlled migration execution in production environments
- Include database versioning checks during application startup
- Generate database documentation from Liquibase changesets

## Dependency Management & Tooling
- Use Maven BOM (Bill of Materials) to ensure consistent dependency versions.
- Favor library versions that align with LTS support.
- Scan dependencies for vulnerabilities (e.g., OWASP Dependency Check).
- The root pom should contain the versions of the libraries used by the apps and libs
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

## Contract Testing
- Implement consumer-driven contract testing using Pact
- Derive contracts from OpenAPI specifications to ensure alignment
- Each service maintains contracts for dependencies it consumes
- Provider services verify they meet consumer expectations

### Consumer-Side Contract Testing
- Define Pact contracts in consumer tests based on expected provider behavior
- Map OpenAPI schemas to Pact interactions for consistency
- Generate Pact files during consumer test execution
- Publish Pact contracts to Pact Broker for sharing with providers
- Organize contract tests in `src/test/java/<package>/contracts/consumer` directory

### Provider-Side Contract Testing
- Verify provider compliance with published Pact contracts
- Use provider state handlers to set up test data for scenarios
- Implement automated contract verification in CI pipeline
- Block releases when contracts are broken
- Organize provider verification tests in `src/test/java/<package>/contracts/provider` directory

### Pact Workflow Integration

- Implement contract testing as part of automated test suite
- Tag verified contracts with version and environment information
- Establish matrix of compatibility between service versions

### Contract Testing Guidelines
- Focus on API boundaries and service interfaces
- Keep contracts focused on format rather than business logic
- Use provider states to set up test scenarios
- Implement version tagging for contract evolution
- Document breaking changes and provide migration paths

## Event-Driven Architecture with AsyncAPI
- Define all event interfaces using AsyncAPI specifications
- Store AsyncAPI specifications in the `api/events` folder for each service
- Generate Kafka producers and consumers from AsyncAPI specs
- Validate events against AsyncAPI schemas
- Use Avro for message serialization with schema registry
- Implement event versioning and compatibility strategy
- Document event payload structures and semantics
- Establish event ownership and responsibility boundaries

## Example Service Structure

```
apps/
├── service-a/
│   ├── api/
│   │   ├── rest/
│   │   │   └── openapi.yaml
│   │   └── events/
│   │       └── asyncapi.yaml
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── com/example/servicea/
│   │   │   │       ├── api/
│   │   │   │       │   ├── rest/
│   │   │   │       │   └── events/
│   │   │   │       │       ├── producers/
│   │   │   │       │       └── consumers/
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
│   │   │   │       │   │   ├── OpenTelemetryConfig.java
│   │   │   │       │   │   └── KafkaConfig.java
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
│   │               ├── adapters/
│   │               │   ├── persistence/
│   │               │   ├── api/
│   │               │   │   ├── rest/
│   │               │   │   └── events/
│   │               │   │       ├── producers/
│   │               │   │       └── consumers/
│   │               │   └── external/
│   │               └── contracts/
│   │                   ├── consumer/
│   │                   │   ├── ServiceBContractTest.java
│   │                   │   └── ServiceCContractTest.java
│   │                   ├── provider/
│   │                   │   ├── ProviderStateHandler.java
│   │                   │   ├── ServiceAContractVerificationTest.java
│   │                   │   └── pact-verifier.properties
│   │                   └── schemas/
│   │                       ├── openapi-to-pact-mappings.json
│   │                       └── asyncapi-to-pact-mappings.json
│   ├── pact/
│   │   ├── consumer/
│   │   │   └── service-a-service-b.json
│   │   └── provider/
│   │       └── service-c-service-a.json
│   └── pom.xml
├── service-b/
│   └── ...
└── service-c/
    └── ...
libs/
├── common/
├── api-clients/
│   ├── rest/
│   └── events/
├── shared-model/
├── contract-testing/
│   ├── openapi-pact-generator/
│   ├── asyncapi-pact-generator/
│   └── contract-test-utils/
└── asyncapi-tools/
    ├── generators/
    └── validators/
config/
├── shared-config/
├── kafka/
│   ├── topics.yaml
│   └── schema-registry.yaml
├── pact/
│   ├── broker-config.yml
│   └── verification-rules.json
└── asyncapi/
    └── common-message-library.yaml
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

## Contract Testing Setup for Services

Each service should include the following contract testing components:

1. **Contract Test Organization**:
   - Consumer contract tests in `src/test/java/<package>/contracts/consumer`
   - Provider verification tests in `src/test/java/<package>/contracts/provider`
   - Schema mappings in `src/test/java/<package>/contracts/schemas`
   - Generated Pact files in the `pact` directory at the service root

2. **Consumer-Side Components**:
   - Contract test classes for each provider the service depends on
   - OpenAPI to Pact schema mapping configurations
   - Utility classes for setting up contract test expectations

3. **Provider-Side Components**:
   - Provider state handlers to set up test scenarios
   - Contract verification test configurations
   - Provider verification properties

4. **Shared Contract Testing Utilities**:
   - Common utilities in `libs/contract-testing/contract-test-utils`
   - OpenAPI to Pact converter in `libs/contract-testing/openapi-pact-generator`
   - Centralized Pact Broker configuration in `config/pact`

5. **Integration with CI/CD**:
   - Contract verification as part of the build pipeline
   - Pact Broker integration for storing and retrieving contracts
   - "Can-I-deploy" checks before service deployment

## AsyncAPI Setup for Event-Driven Interfaces

Each service should include the following AsyncAPI components:

1. **AsyncAPI Specifications**:
   - AsyncAPI YAML files in the `api/events` directory
   - Define all events produced and consumed by the service
   - Include message schemas, channels, and operation bindings
   - Use semantic versioning for API evolution

2. **Code Generation**:
   - Generate Kafka producers and consumers from AsyncAPI specs
   - Create DTOs for event payloads
   - Generate validation code for messages

3. **Event Structure**:
   - Define clear event naming conventions
   - Include metadata in all events (timestamp, source, correlation IDs)
   - Link event schemas to Avro schemas in schema registry
   - Implement event versioning strategy

4. **Testing Event Interfaces**:
   - Write tests for event producers and consumers
   - Validate events against AsyncAPI schemas
   - Use Testcontainers for Kafka and schema registry testing
   - Implement contract tests for event interfaces

5. **Integration with OpenTelemetry**:
   - Add trace context to event headers
   - Implement distributed tracing across event producers and consumers
   - Monitor Kafka metrics through OpenTelemetry
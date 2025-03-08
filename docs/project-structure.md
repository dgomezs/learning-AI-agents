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

## Security Considerationsion
- Implement proper input validation
- Use parameterized queries to prevent SQL injectionng of external dependencies
- Apply authentication and authorization where necessaryostgreSQL for closer production parity
- Protect sensitive datansion model for container lifecycle management
- Implement API security as defined in OpenAPI specs (OAuth2, API keys, etc.)
- Configure shared container instances for the entire test suite when appropriate
## Performance Guidelinesest containers for:
- Optimize database queriesting
- Implement caching where appropriatent-driven components
- Consider asynchronous processing for long-running tasks
- Profile and optimize critical code paths
- Optimize Kafka topic partitioning for parallel processing
  - Any other external dependencies
## Database Migration Strategy
- Use Liquibase for database schema evolution and versioning
- Store changelog files in a dedicated directory structure:ing production
  - `src/main/resources/db/changelog/` for main changelog filesrtup
  - Use semantic versioning for changelog files (e.g., `v1.0.0.xml`, `v1.1.0.xml`)
- Organize changes by type and entity:e database readiness before tests
  - `db/changelog/changes/tables/` - Table creation and modificationsce
  - `db/changelog/changes/sequences/` - Sequence definitionsk or table truncation)
  - `db/changelog/changes/data/` - Reference/seed data
  - `db/changelog/changes/constraints/` - Foreign keys and constraints
  - `db/changelog/changes/indexes/` - Index creationr, Schema Registry)
- Follow naming conventions:lpers for topic creation and event verification
  - `YYYYMMDDHHMMSS_short-description.xml` for change filesess
- Include descriptive comments for each changesetidation
- Implement rollback procedures for all changeswith actual Avro schemas
- Test migrations in CI pipeline using an in-memory databasencompatibility)
- Separate DDL (Data Definition Language) from DML (Data Manipulation Language)
- Run migrations automatically during application startup in development
- Use controlled migration execution in production environments
- Include database versioning checks during application startup
- Generate database documentation from Liquibase changesets
- Use container networks for multi-container tests
## Dependency Management & Toolingration for debugging
- Use Maven BOM (Bill of Materials) to ensure consistent dependency versions.
- Favor library versions that align with LTS support.limits, cleanup policies)
- Scan dependencies for vulnerabilities (e.g., OWASP Dependency Check).
- The root pom should contain the versions of the libraries used by the apps and libs
- Ensure builds pass with `mvn verify` before pushing changes.
- Define container classes in `src/test/java/<package>/test/containers/` 
## Observability Stackt utilities for container interaction in `libs/test-utils`
- Implement the three pillars of observability: logs, metrics, and traces
- Use OpenTelemetry as the vendor-agnostic observability frameworkios
- Export telemetry data to your preferred backends (Jaeger, Prometheus, Elasticsearch, etc.)
- Follow W3C trace context specification for consistent cross-service tracing
- Implement proper input validation
## Loggingmeterized queries to prevent SQL injection
- Use structured logging with JSON format (Logback + SLF4J)
- Set appropriate log levels (`DEBUG`, `INFO`, `WARN`, `ERROR`)
- Include trace and span IDs in log entries for correlation2, API keys, etc.)
- Centralize logs in a searchable platform (e.g., Elasticsearch)
## Performance Guidelines
## Tracing & Metrics with OpenTelemetry
- Implement distributed tracing across all services
- Configure OpenTelemetry SDK in each serviceunning tasks
- Automatically instrument frameworks and libraries where possible
- Add custom instrumentation for business-critical operations
- Create custom spans for important business transactions
- Define and collect custom metrics for business KPIs
- Expose Prometheus endpoint in each servicen and versioning
- Track the four golden signals: latency, traffic, errors, and saturation
- Set up dashboards for visualizing metrics and traceslog files
  - Use semantic versioning for changelog files (e.g., `v1.0.0.xml`, `v1.1.0.xml`)
## Error Handling & Resilience entity:
- Define a global exception handling strategyeation and modifications
- Use retry mechanisms for transient failuresnce definitions
- Implement graceful degradation (e.g., circuit breakers with Resilience4j)
- Set up alerts based on error rates and SLOseign keys and constraints
  - `db/changelog/changes/indexes/` - Index creation
## Contract Testingventions:
- Implement consumer-driven contract testing using Pactiles
- Derive contracts from OpenAPI specifications to ensure alignment
- Each service maintains contracts for dependencies it consumes
- Provider services verify they meet consumer expectationsse
- Separate DDL (Data Definition Language) from DML (Data Manipulation Language)
### Consumer-Side Contract Testinging application startup in development
- Define Pact contracts in consumer tests based on expected provider behavior
- Map OpenAPI schemas to Pact interactions for consistencyartup
- Generate Pact files during consumer test executionngesets
- Publish Pact contracts to Pact Broker for sharing with providers
- Organize contract tests in `src/test/java/<package>/contracts/consumer` directory
- Use Maven BOM (Bill of Materials) to ensure consistent dependency versions.
### Provider-Side Contract Testingn with LTS support.
- Verify provider compliance with published Pact contractsdency Check).
- Use provider state handlers to set up test data for scenariosd by the apps and libs
- Implement automated contract verification in CI pipelineges.
- Block releases when contracts are broken
- Organize provider verification tests in `src/test/java/<package>/contracts/provider` directory
- Implement the three pillars of observability: logs, metrics, and traces
### Pact Workflow Integrationndor-agnostic observability framework
- Export telemetry data to your preferred backends (Jaeger, Prometheus, Elasticsearch, etc.)
- Implement contract testing as part of automated test suites-service tracing
- Tag verified contracts with version and environment information
- Establish matrix of compatibility between service versions
- Use structured logging with JSON format (Logback + SLF4J)
### Contract Testing GuidelinesDEBUG`, `INFO`, `WARN`, `ERROR`)
- Focus on API boundaries and service interfacescorrelation
- Keep contracts focused on format rather than business logicch)
- Use provider states to set up test scenarios
- Implement version tagging for contract evolution
- Document breaking changes and provide migration paths
- Configure OpenTelemetry SDK in each service
## Event-Driven Architecture with AsyncAPIlibraries where possible
- Define all event interfaces using AsyncAPI specificationsns
- Store AsyncAPI specifications in the `api/events` folder for each service
- Generate Kafka producers and consumers from AsyncAPI specs
- Validate events against AsyncAPI schemasce
- Use Avro for message serialization with schema registry, and saturation
- Implement event versioning and compatibility strategy
- Document event payload structures and semantics
- Establish event ownership and responsibility boundaries
- Define a global exception handling strategy
## Example Service Structureransient failures
- Implement graceful degradation (e.g., circuit breakers with Resilience4j)
```et up alerts based on error rates and SLOs
apps/
├── service-a/sting
│   ├── api/consumer-driven contract testing using Pact
│   │   ├── rest/s from OpenAPI specifications to ensure alignment
│   │   │   └── openapi.yamltracts for dependencies it consumes
│   │   └── events/ verify they meet consumer expectations
│   │       └── asyncapi.yaml
│   ├── src/-Side Contract Testing
│   │   ├── main/tracts in consumer tests based on expected provider behavior
│   │   │   ├── java/ to Pact interactions for consistency
│   │   │   │   └── com/example/servicea/t execution
│   │   │   │       ├── api/Pact Broker for sharing with providers
│   │   │   │       │   ├── rest//test/java/<package>/contracts/consumer` directory
│   │   │   │       │   └── events/
│   │   │   │       │       ├── producers/
│   │   │   │       │       └── consumers/d Pact contracts
│   │   │   │       ├── application/ up test data for scenarios
│   │   │   │       │   └── usecases/cation in CI pipeline
│   │   │   │       │       ├── CreateUserCommand.java
│   │   │   │       │       ├── UpdateUserCommand.javava/<package>/contracts/provider` directory
│   │   │   │       │       ├── GetUserByIdQuery.java
│   │   │   │       │       └── ListUsersQuery.java
│   │   │   │       ├── domain/
│   │   │   │       │   ├── model/rt of automated test suite
│   │   │   │       │   ├── events/on and environment information
│   │   │   │       │   └── services/etween service versions
│   │   │   │       ├── infrastructure/
│   │   │   │       │   ├── config/
│   │   │   │       │   │   ├── OpenTelemetryConfig.java
│   │   │   │       │   │   └── KafkaConfig.javausiness logic
│   │   │   │       │   ├── persistence/narios
│   │   │   │       │   ├── kafka/ntract evolution
│   │   │   │       │   │   ├── producers/gration paths
│   │   │   │       │   │   └── consumers/
│   │   │   │       │   ├── external/ncAPI
│   │   │   │       │   └── observability/PI specifications
│   │   │   │       │       ├── CustomMetrics.java` folder for each service
│   │   │   │       │       ├── TracingAspect.javacAPI specs
│   │   │   │       │       └── MetricsService.java
│   │   │   └── resources/ialization with schema registry
│   │   │       ├── application.propertieslity strategy
│   │   │       ├── otel-collector-config.yamlics
│   │   │       └── avro/ip and responsibility boundaries
│   │   │           └── user-events.avsc
│   │   └── test/e Structure
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
│   │               └── contracts//
│   │                   ├── consumer/cers/
│   │                   │   ├── ServiceBContractTest.java
│   │                   │   └── ServiceCContractTest.java
│   │                   ├── provider/
│   │                   │   ├── ProviderStateHandler.java
│   │                   │   ├── ServiceAContractVerificationTest.java
│   │                   │   └── pact-verifier.properties
│   │                   └── schemas/UsersQuery.java
│   │                       ├── openapi-to-pact-mappings.json
│   │                       └── asyncapi-to-pact-mappings.json
│   ├── pact/       │   ├── events/
│   │   ├── consumer/   └── services/
│   │   │   └── service-a-service-b.json
│   │   └── provider/   ├── config/
│   │       └── service-c-service-a.jsonmetryConfig.java
│   └── pom.xml     │   │   └── KafkaConfig.java
├── service-b/      │   ├── persistence/
│   └── ... │       │   ├── kafka/
└── service-c/      │   │   ├── producers/
    └── ... │       │   │   └── consumers/
libs/   │   │       │   ├── external/
├── common/ │       │   └── observability/
├── api-clients/    │       ├── CustomMetrics.java
│   ├── rest/       │       ├── TracingAspect.java
│   └── events/     │       └── MetricsService.java
├── shared-model/esources/
├── contract-testing/pplication.properties
│   ├── openapi-pact-generator/tor-config.yaml
│   ├── asyncapi-pact-generator/
│   └── contract-test-utils/-events.avsc
└── asyncapi-tools/
    ├── generators/a/
    └── validators/ com/example/servicea/
config/             ├── usecases/
├── shared-config/  │   ├── CreateUserCommandTest.java
├── kafka/          │   ├── UpdateUserCommandTest.java
│   ├── topics.yaml │   ├── GetUserByIdQueryTest.java
│   └── schema-registry.yamlListUsersQueryTest.java
├── pact/           ├── adapters/
│   ├── broker-config.yml── persistence/
│   └── verification-rules.json/
└── asyncapi/       │   │   ├── rest/
    └── common-message-library.yamlnts/
``` │               │   │       ├── producers/
│   │               │   │       └── consumers/
## OpenTelemetry Setup for Servicesl/
│   │               └── contracts/
Each service should include the following OpenTelemetry components:
│   │                   │   ├── ServiceBContractTest.java
│   │                   │   └── ServiceCContractTest.java
   - OpenTelemetry API and SDKovider/
   - Auto-instrumentation agents for:derStateHandler.java
     - Web frameworks (JAX-RS, Servlet)AContractVerificationTest.java
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
   - Correlation IDs in logs linked to trace IDs
├── contract-testing/
│   ├── openapi-pact-generator/
│   ├── asyncapi-pact-generator/
│   └── contract-test-utils/
└── asyncapi-tools/
    ├── generators/
    └── validators/
config/
   - Provider verification tests in `src/test/java/<package>/contracts/provider`
   - Schema mappings in `src/test/java/<package>/contracts/schemas`
   - Generated Pact files in the `pact` directory at the service root
│   └── schema-registry.yaml
2. **Consumer-Side Components**:
   - Contract test classes for each provider the service depends on
│   └── verification-rules.json
   - Utility classes for setting up contract test expectations
    └── common-message-library.yaml
3. **Provider-Side Components**:
   - Provider state handlers to set up test scenarios
   - Contract verification test configurations
   - Provider verification properties
Each service should include the following OpenTelemetry components:
4. **Shared Contract Testing Utilities**:
   - Common utilities in `libs/contract-testing/contract-test-utils`
   - OpenAPI to Pact converter in `libs/contract-testing/openapi-pact-generator`
   - Centralized Pact Broker configuration in `config/pact`
     - Web frameworks (JAX-RS, Servlet)
5. **Integration with CI/CD**:
   - Contract verification as part of the build pipeline
   - Pact Broker integration for storing and retrieving contracts
   - "Can-I-deploy" checks before service deployment
2. **Configuration**:
## AsyncAPI Setup for Event-Driven Interfaces and exporter configuration
   - Environment-specific settings in application properties
Each service should include the following AsyncAPI components:
3. **Custom Instrumentation**:
1. **AsyncAPI Specifications**:OP-based method tracing
   - AsyncAPI YAML files in the `api/events` directorys
   - Define all events produced and consumed by the serviceon
   - Include message schemas, channels, and operation bindings
   - Use semantic versioning for API evolution
   - `otel-collector-config.yaml` defining receivers, processors, and exporters
2. **Code Generation**: for sidecar or centralized collector
   - Generate Kafka producers and consumers from AsyncAPI specs
   - Create DTOs for event payloads
   - Generate validation code for messagesice boundaries
   - Correlation IDs in logs linked to trace IDs
3. **Event Structure**:sing telemetry status
   - Define clear event naming conventions
   - Include metadata in all events (timestamp, source, correlation IDs)
   - Link event schemas to Avro schemas in schema registry
   - Implement event versioning strategyg contract testing components:

4. **Testing Event Interfaces**:*:
   - Write tests for event producers and consumersckage>/contracts/consumer`
   - Validate events against AsyncAPI schemas/java/<package>/contracts/provider`
   - Use Testcontainers for Kafka and schema registry testinghemas`
   - Implement contract tests for event interfacesat the service root

5. **Integration with OpenTelemetry**:
   - Add trace context to event headersvider the service depends on
   - Implement distributed tracing across event producers and consumers
   - Monitor Kafka metrics through OpenTelemetryt expectations

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
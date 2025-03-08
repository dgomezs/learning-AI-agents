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
- **Generate Request/Response DTOs from OpenAPI specifications:**
  - Use OpenAPI Generator to generate model classes from API specs
  - Configure generation to produce immutable objects with builders
  - Store generated models in `api/rest/generated` package
  - Never modify generated code directly; update the OpenAPI spec instead
  - Regenerate models whenever OpenAPI specs change
- **Generate Request/Response DTOs from OpenAPI specifications:**
  - Use OpenAPI Generator Maven plugin to generate model classes from API specs
  - Configure in pom.xml to run during build phase
  - Generated code must be placed in `target/generated-sources`
  - Store generated models in `com.example.<service>.api.rest.model` package
  - Generate controller interfaces in `com.example.<service>.api.rest` package
  - Never modify generated code directly; update the OpenAPI spec instead
  - Regenerate models whenever OpenAPI specs change
- **Controller implementation:**
  - Implement controller interfaces generated from OpenAPI specs
  - Use the generated Request/Response classes in controller implementations
  - Map between generated DTOs and domain models using mappers
  - Place mappers in `api/rest/mappers` package
- **Controller implementation:**
  - Implement controller interfaces generated from OpenAPI specs
  - Create implementation classes in `com.example.<service>.api.rest` package
  - Use the generated Request/Response classes in controller implementations
  - Map between generated DTOs and domain models using mappers
  - Place mappers in `com.example.<service>.api.rest.mappers` package
  - Controllers should delegate to use cases and never contain business logic
- **Validation:**
  - Leverage validation annotations generated from OpenAPI specs
  - Add custom validators for complex business rules
  - Validate requests before processing in use cases
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

## Security Considerations
- Implement proper input validation
- Use parameterized queries to prevent SQL injection
- Protect sensitive data through proper handling and encryption
- Implement request rate limiting to prevent DoS attacks

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
- Test migrations in CI pipeline
- Separate DDL (Data Definition Language) from DML (Data Manipulation Language)
- Run migrations automatically during application startup in development
- Use controlled migration execution in production environments
- Include database versioning checks during application startup
- Generate database documentation from Liquibase changesets

## Dependency Management & Tooling
- Use Maven BOM (Bill of Materials) to ensure consistent dependency versions
- Favor library versions that align with LTS support
- Scan dependencies for vulnerabilities (e.g., OWASP Dependency Check)
- The root pom should contain the versions of the libraries used by the apps and libs
- Ensure builds pass with `mvn verify` before pushing changes
- Define container classes in `src/test/java/<package>/test/containers/`

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

## Testing Guidelines
- Organize tests by:
  - Use Cases: Testing application logic in isolation. Use @QuarkusTest for these tests
  - Adapters: Testing integration with external boundaries. Use @QuarkusIntegrationTest for these tests
- Write unit tests for all use cases (both commands and queries)
- Implement integration tests for repositories and external services
- Create API tests validating against OpenAPI contracts
- Test Kafka producers and consumers using test containers
- Aim for at least 80% code coverage
- Follow the Given/When/Then structure
- For unit tests follow naming convention: [Class][Test] (e.g., CreateBrandCommandTest)
- For integration tests follow naming convention: [Class][IT] (e.g., BrandRepositoryIT)

### Mock Verification Best Practices

1. **Complete Argument Verification:**
   - Always verify all fields/properties of objects passed to mocked methods
   - Use `argThat` with explicit type casting for better type safety
   - Compare every field that is relevant to the business logic

```java
// ❌ Incomplete verification
verify(eventPublisher).publish(argThat(event -> 
    event.getId().equals(expectedId)
));

// ✅ Complete verification
verify(eventPublisher).publish(argThat((SomeEvent event) -> 
    event.getId().equals(expectedId) &&
    event.getName().equals(expectedName) &&
    event.getTimestamp().equals(expectedTimestamp) &&
    event.getDetails().equals(expectedDetails)
));
```

2. **Custom Matchers:**
   - Create dedicated argument matchers for complex objects
   - Put matchers in a separate class under `test/java/matchers`
   - Name matcher classes with suffix `Matcher`

```java
public class BrandCreatedEventMatcher implements ArgumentMatcher<BrandCreatedEvent> {
    private final Brand expectedBrand;

    public BrandCreatedEventMatcher(Brand expectedBrand) {
        this.expectedBrand = expectedBrand;
    }

    @Override
    public boolean matches(BrandCreatedEvent event) {
        return event.getBrandId().equals(expectedBrand.getId()) &&
               event.getName().equals(expectedBrand.getName()) &&
               event.getDescription().equals(expectedBrand.getDescription());
    }
}

// Usage in test
verify(eventPublisher).publish(argThat(new BrandCreatedEventMatcher(expectedBrand)));
```

3. **Verification Order:**
   - Use `InOrder` verification when order of calls matters
   - Verify all interactions with mocks
   - Verify no unexpected interactions occurred

```java
// ✅ Proper order verification
InOrder inOrder = inOrder(repository, eventPublisher);
inOrder.verify(repository).save(expectedEntity);
inOrder.verify(eventPublisher).publish(expectedEvent);
inOrder.verifyNoMoreInteractions();
```

4. **Error Messages:**
   - Provide descriptive error messages in argument matchers
   - Use AssertJ's SoftAssertions for multiple verifications

```java
verify(eventPublisher).publish(argThat(event -> {
    SoftAssertions softly = new SoftAssertions();
    softly.assertThat(event.getId())
          .as("Event ID should match")
          .isEqualTo(expectedId);
    softly.assertThat(event.getName())
          .as("Event name should match")
          .isEqualTo(expectedName);
    softly.assertAll();
    return true;
}));
```

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
- Organize provider verification tests in `src/test/java/<package>/contracts/provider` directory

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
- Define a global exception handling strategy

## API-Generated Code Management
- **Version Management:**
  - Include API version in generated package names (e.g., `v1`, `v2`)
  - Maintain backward compatibility between versions
  - Document breaking changes in API changelog
- **Build & Generation Process:**
  - Configure Maven plugin for OpenAPI generation during build
  - Run API generation as part of the build process
  - Fail the build if API specs are inconsistent with implementations
  - Document generation process in project README
  - Ensure all team members understand how to regenerate API code
  - Consider using Git hooks to ensure specs and code stay synchronized
- **Synchronization:**
  - Keep API specs as the source of truth
  - Ensure generated code and API specs remain in sync
  - Implement workflows to update specs when business requirements change
  - Review and approve API changes before implementation


## API Error Response Standards

### Error Response Format
All API error responses must follow RFC 7807 (Problem Details for HTTP APIs) specification and include these required fields:

- `type` - URI reference identifying the problem type
- `title` - Short, human-readable problem summary
- `status` - HTTP status code
- `detail` - Human-readable problem explanation
- `timestamp` - Error occurrence time (ISO 8601 format)

Optional but recommended fields:
- `path` - Request path that caused the error
- `instance` - URI reference for specific error occurrence
- `validationErrors` - Array of field-level validation errors

### OpenAPI Schema Template
Use this schema template for all API error responses:

```yaml
ErrorResponse:
  type: object
  description: Problem Details for HTTP APIs (RFC 7807)
  required:
    - type
    - title
    - status
    - detail
    - timestamp
  properties:
    type:
      type: string
      description: A URI reference that identifies the problem type
      example: "https://api.product-catalog.com/errors/validation-error"
      format: uri
    title:
      type: string
      description: A short, human-readable summary of the problem type
      example: "Validation Error"
    status:
      type: integer
      description: The HTTP status code
      example: 400
    detail:
      type: string
      description: A human-readable explanation specific to this occurrence of the problem
      example: "Brand name must not be empty and must be less than 100 characters"
    timestamp:
      type: string
      format: date-time
      description: The time when the error occurred
      example: "2024-01-20T10:30:00Z"
    path:
      type: string
      description: The request path that caused the error
      example: "/api/v1/brands"
    instance:
      type: string
      description: A URI reference that identifies the specific occurrence of the problem
      example: "/errors/12345"
      format: uri
    validationErrors:
      type: array
      description: List of field-level validation errors
      items:
        type: object
        properties:
          field:
            type: string
            example: "name"
          message:
            type: string
            example: "must not be empty"
          rejectedValue:
            type: string
            example: ""
```

### Usage Guidelines

1. Always use `application/problem+json` as the media type for error responses
2. Include error responses for all possible error status codes (400, 401, 403, 404, etc.)
3. Provide meaningful examples in the OpenAPI spec
4. Use consistent error types across all APIs (maintain a shared error type registry)
5. Include validation errors array for 400 Bad Request responses
6. Use ISO 8601 format for all timestamps

### Example Implementation
```yaml
responses:
  '400':
    description: Invalid input
    content:
      application/problem+json:
        schema:
          $ref: '#/components/schemas/ErrorResponse'
```

## Example Service Structure
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
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── otel-collector-config.yaml
│   │       └── avro/
│   │           └── user-events.avsc
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




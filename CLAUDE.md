# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Development Commands

### Building and Testing
- **Full build and test**: `mvn clean verify`
- **Compile only**: `mvn compile`
- **Run unit tests**: `mvn test`
- **Run integration tests**: `mvn failsafe:integration-test`
- **Run single test**: `mvn test -Dtest=ClassName#methodName`
- **Run single integration test**: `mvn failsafe:integration-test -Dit.test=ClassNameIT#methodName`

### Development Server (Product Catalog Service)
- **Start dev mode**: `cd apps/product-catalog && mvn quarkus:dev`
- **Build native image**: `mvn package -Pnative`
- **Run with Docker**: `docker-compose up`

### Code Generation
- **Generate OpenAPI models**: `mvn generate-sources` (runs OpenAPI generator for REST APIs)
- **Generate AsyncAPI models**: `mvn generate-sources` (runs AsyncAPI generator for events)

## Project Architecture

This is a Java 21 monorepo using Quarkus framework with clean architecture and CQRS pattern. The architecture follows event-driven design with Kafka messaging.

### Key Architecture Patterns
- **Clean Architecture**: Separation between API, Application (use cases), Domain, and Infrastructure layers
- **CQRS**: Commands change state, Queries read state - each as dedicated classes in `usecases/`
- **API-First**: OpenAPI specs drive REST API generation, AsyncAPI specs drive event contracts
- **Event-Driven**: Kafka for async communication between services

### Directory Structure
```
apps/                          # Microservices
├── product-catalog/          # Product catalog service
│   ├── api/rest/openapi.yaml # REST API specification
│   ├── api/events/asyncapi.yaml # Event specification
│   ├── docs/                 # Service-specific documentation
│   │   ├── *-models.md       # Domain model specifications (can be multiple files)
│   │   └── *-use-cases.md    # Use cases in Gherkin format (can be multiple files)
│   └── src/main/java/com/example/productcatalog/
│       ├── api/rest/         # REST controllers (generated + implemented)
│       ├── application/usecases/ # Commands and Queries
│       ├── domain/model/     # Business entities
│       └── infrastructure/   # External integrations
libs/                         # Shared libraries
├── common/                   # Common utilities
└── shared-model/            # Shared data models
config/                       # Shared configuration
└── kafka/topics.yaml        # Kafka topic definitions
```

### Technology Stack
- **Framework**: Quarkus 3.4.1
- **Language**: Java 21
- **Database**: PostgreSQL with Hibernate ORM Panache
- **Messaging**: Apache Kafka with Avro serialization
- **Testing**: JUnit 5, Mockito, TestContainers, Pact for contract testing
- **Build**: Maven with multi-module structure
- **Observability**: OpenTelemetry for tracing and metrics

## Code Conventions

### Use Case Naming
- Commands: `[Action][Entity]Command` (e.g., `CreateBrandCommand`)
- Queries: `[Action][Entity]Query` (e.g., `GetBrandByIdQuery`)
- All use cases in `application/usecases/` package

### Testing Conventions
- Unit tests: `[Class]Test.java` using `@QuarkusTest`
- Integration tests: `[Class]IT.java` using `@QuarkusIntegrationTest`
- Contract tests in `contracts/consumer/` and `contracts/provider/` packages
- Test containers in `test/containers/` package

### Dependency Injection
- Use `@ApplicationScoped` for services
- Constructor injection with `final` fields
- Lombok `@RequiredArgsConstructor` for DI

### Database Migrations
- Liquibase for schema management
- Changesets in `src/main/resources/db/changelog/changes/`
- Organized by type: `tables/`, `sequences/`, `constraints/`, `indexes/`
- Naming: `YYYYMMDDHHMMSS_description.xml`

### Maven Dependency Management
- **All plugin and library versions must be defined in the root `pom.xml`**
- Child modules should inherit versions from parent POM to ensure consistency
- Never specify versions directly in child module POMs unless absolutely necessary
- Use `<dependencyManagement>` and `<pluginManagement>` sections in root POM

## Important Development Notes

### Service Documentation and Code Generation
- **Service Documentation**: Each service in `apps/` contains a `docs/` folder with specifications
  - `*-models.md`: Domain model specifications defining entities and their properties (multiple files allowed)
  - `*-use-cases.md`: Use cases written in Gherkin format describing business scenarios (multiple files allowed)
  - These documents are the **single source of truth** for implementing business logic
  - **ALWAYS review ALL documentation files** in the service's `docs/` folder before implementing features or APIs
  - OpenAPI and AsyncAPI specifications should align with these domain specifications

### OpenAPI Code Generation
- REST API models are generated from `api/rest/openapi.yaml`
- Generated code in `target/generated-sources/openapi/`
- Implement generated interfaces in `api/rest/` package
- **Follow API design standards in `docs/api-guidelines.md`**:
  - Use RFC 7807 Problem Details for error responses
  - Follow Zalando RESTful API Guidelines
  - Use camelCase for JSON properties
  - Use hyphen-case for URIs
  - Include proper HTTP status codes and validation

### Event-Driven Architecture
- AsyncAPI specs define event contracts in `api/events/asyncapi.yaml`
- Kafka producers/consumers in `infrastructure/events/`
- Event publishers use simple interfaces for testing

### Testing Strategy
- Mock verification must verify ALL relevant object fields
- Use `argThat` with explicit type casting for mock verification
- Integration tests use TestContainers for PostgreSQL and Kafka
- Contract testing with Pact for service boundaries
- Unit test mock only external, slow dependencies (Database, file system)
- Tests focused on use cases not specific classes or functions

### Common Development Workflow
1. Define API contracts first (OpenAPI/AsyncAPI)
2. Run `mvn generate-sources` to generate models
3. Implement use cases in `application/usecases/`
4. Add infrastructure implementations
5. Write comprehensive tests with full mock verification
6. Run `mvn clean verify` before committing
7. Follow Conventional Commits format for commit messages

### Commit Message Format
This project follows [Conventional Commits](https://www.conventionalcommits.org/) specification:

```
<type>[optional scope]: <description>

[optional body]

[optional footer(s)]
```

**Types**:
- `feat`: new feature
- `fix`: bug fix
- `docs`: documentation changes
- `style`: formatting, missing semicolons, etc.
- `refactor`: code changes that neither fix bugs nor add features
- `test`: adding or modifying tests
- `chore`: maintenance tasks, dependency updates, etc.

**Examples**:
- `feat(product-catalog): add brand creation endpoint`
- `fix(auth): resolve token validation issue`
- `docs: update API documentation for user service`
- `test(brand): add integration tests for brand repository`
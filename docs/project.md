# Project Instructions for GitHub Copilot

## Project Overview
This is a Java-based monorepo containing multiple microservices. Each service follows clean architecture principles with a clear separation of commands and queries (CQRS). The project aims to be maintainable, testable, and scalable.

## Technology Stack
- Java 21 (LTS)
- Quarkus framework
- RESTful API design
- Maven for dependency management
- JUnit 5 for testing
- PostgreSQL for database

## Monorepo Structure
- Organized with multiple services in the apps folder
- Shared libraries and utilities in the libs folder
- Common configuration in the config folder

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
  - API/Controller (External interfaces)
  - Application (Use cases implementing commands and queries)
  - Domain (Business logic and entities)
  - Infrastructure (External services, databases, messaging)

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
- Create API tests for controllers
- Aim for at least 80% code coverage

## Security Considerations
- Implement proper input validation
- Use parameterized queries to prevent SQL injection
- Apply authentication and authorization where necessary
- Protect sensitive data

## Performance Guidelines
- Optimize database queries
- Implement caching where appropriate
- Consider asynchronous processing for long-running tasks
- Profile and optimize critical code paths

## Example Service Structure
```
apps/
├── service-a/
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
│   │   │   │       └── infrastructure/
│   │   │   │           ├── config/
│   │   │   │           ├── persistence/
│   │   │   │           └── external/
│   │   │   └── resources/
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
│   │                   └── external/
│   └── pom.xml
├── service-b/
│   └── ...
└── service-c/
    └── ...
libs/
├── common/
└── shared-model/
config/
└── shared-config/
```

When generating code, please follow these guidelines and structure to maintain consistency across the project.

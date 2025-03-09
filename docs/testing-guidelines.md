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


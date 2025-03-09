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
  - Use OpenAPI 3.0.0
  - Use OpenAPI Generator Maven plugin to generate model classes from API specs
  - Configure in pom.xml to run during build phase
  - Generated code must be placed in `target/generated-sources`
  - Store generated models in `com.example.<service>.api.rest.model` package
  - Generate controller interfaces in `com.example.<service>.api.rest` package
  - Never modify generated code directly; update the OpenAPI spec instead
  - Regenerate models whenever OpenAPI specs change
- **Generate Event Classes from AsyncAPI specifications:**
  - Use AsyncAPI Generator Maven plugin to generate event classes
  - Configure generation in pom.xml to run during build phase
  - Generated code must be placed in `target/generated-sources`
  - Never modify generated event classes directly; update AsyncAPI spec instead
  - Don't include versions in the event name. Use headers  
  - Event classes should include serialization configuration for Kafka
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

# API Design Standards

## RESTful API Guidelines

We follow the [Zalando RESTful API Guidelines](https://opensource.zalando.com/restful-api-guidelines/) for designing our APIs. Key principles include:

### Must-Follow Rules

1. **Use JSON as the Primary Data Format**
   - Use `application/json` as the default content type
   - Use camelCase for property names
   - Don't use null values for optional properties

2. **Use Standard HTTP Methods**
   - GET (read)
   - POST (create)
   - PUT (full update)
   - PATCH (partial update)
   - DELETE (remove)

3. **Use Standard HTTP Status Codes**
   - 200 - OK
   - 201 - Created
   - 204 - No Content
   - 400 - Bad Request
   - 401 - Unauthorized
   - 403 - Forbidden
   - 404 - Not Found
   - 409 - Conflict
   - 500 - Internal Server Error

4. **Use Plural Names for Collections**
   - /brands (not /brand)
   - /products (not /product)
   - /categories (not /category)

5. **Use Hyphen-Case for URIs**
   - /product-categories (not /productCategories)
   - /shipping-addresses (not /shippingAddresses)

6. **Use Problem JSON for Error Messages**
   - Follow RFC 7807 for error responses
   - Include problem details in responses
   - Use application/problem+json media type

7. **Versioning**
   - Use URI versioning (e.g., /v1/brands)
   - Never release an API without a version
   - Use semantic versioning for API changes

8. **Pagination**
   - Use cursor-based pagination for large datasets
   - Include next and prev links in responses
   - Support limit parameter for page size

### API Security

1. **Authentication**
   - Use OAuth 2.0 for authentication
   - Use Bearer tokens in Authorization header
   - Support JWT tokens

2. **Rate Limiting**
   - Include rate limit headers
   - Use 429 Too Many Requests status code
   - Provide retry-after header

### API Documentation

1. **OpenAPI Specification**
   - Document all APIs using OpenAPI 3.1
   - Include examples for all operations
   - Document security schemes

2. **AsyncAPI Specification**
   - Document all event-driven APIs using AsyncAPI
   - Include message schemas and examples
   - Document supported protocols and bindings
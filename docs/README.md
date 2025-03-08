# E-Commerce Platform Documentation

## Overview
This project is an e-commerce platform built within a Java-based monorepo containing multiple microservices. It demonstrates the use of GitHub Copilot for developing a complex, real-world application.

## Functionality
The platform provides the following key features:

- **Product Management**: Browse and search for products with advanced filtering
- **Shopping Cart**: Add products to cart, modify quantities, and save for later
- **User Accounts**: Register, login, and manage user profiles
- **Checkout Process**: Multi-step checkout with address validation
- **Payment Processing**: Secure credit card payment integration
- **Order Management**: Track orders and view order history
- **Inventory Management**: Real-time inventory tracking

## Getting Started
To get started with this project, follow these steps:

1. Clone the repository.
2. Install dependencies.
3. Configure the environment variables.
4. Run the application.

## Documentation Structure
The `docs` folder contains important reference materials and guidelines for the project:

- **project.md**: Contains detailed project instructions for GitHub Copilot, including architecture guidelines, coding standards, and folder structure.
- **prompts.md**: Provides example prompts for effectively using GitHub Copilot in your development workflow.
- **CONTRIBUTING.md**: Guidelines for contributing to the project, including the process for submitting pull requests.

## Using GitHub Copilot with This Project
This project is configured to maximize GitHub Copilot's effectiveness:

1. The VS Code settings in `.vscode/settings.json` are configured to reference these documentation files.
2. Copilot is set up to follow the architectural patterns and coding standards defined in `project.md`.
3. Example prompts in `prompts.md` can help you get the most out of Copilot.

### Best Practices for Copilot Instructions

#### Project-Level Instructions
1. **Maintain Detailed Documentation**:
   - Keep `project.md` updated with the latest architectural decisions
   - Document coding patterns with concrete examples
   - Clearly specify folder structures and naming conventions

2. **Configure VS Code Settings**:
   ```json
   "github.copilot.chat.codeGeneration.instructions": [
     { "file": "docs/project.md" }
   ],
   "github.copilot.chat.codeGeneration.useInstructionFiles": true
   ```

3. **Use Multiple Instruction Files**:
   - Split complex guidelines into focused documents
   - Reference all files in your settings

#### File-Level Instructions
1. **Add Context in Comments**:
   - Place detailed comments at the top of key files
   - Reference related files and patterns
   - Explain non-obvious design decisions

2. **Strategic Code Comments**:
   - Mark areas where generated code should follow specific patterns
   - Use TODO comments to guide future code generation

#### Effective Prompts
1. **Be Specific**:
   - Mention the exact pattern to follow: "Generate a Product service following the pattern in UserService.java"
   - Specify architectural constraints: "Create a repository that follows our clean architecture guidelines"

2. **Reference Documentation**:
   - "Following the structure in docs/project.md, create a new command class for user registration"
   - "Using our Kafka consumer pattern documented in project.md, create a listener for order events"

## Project Architecture
The platform follows clean architecture principles with CQRS pattern implementation, as detailed in the `project.md` file. It uses an API-first design approach with OpenAPI specifications and event-driven architecture with Kafka.

### Microservices
- **Product Service**: Manages product catalog and inventory
- **User Service**: Handles user authentication and profile management
- **Cart Service**: Manages shopping cart operations
- **Order Service**: Processes orders and manages order lifecycle
- **Payment Service**: Handles payment processing and integration with payment gateways
- **Search Service**: Provides product search functionality

## Technical Features
- Monorepo structure with multiple services
- Clean architecture with CQRS
- API-first design using OpenAPI
- Event-driven architecture with Kafka
- Comprehensive testing strategy

## Contributing
Please read the [CONTRIBUTING.md](CONTRIBUTING.md) for details on our code of conduct and the process for submitting pull requests.

# Project Documentation

## Overview
This project is a sample project to demonstrate the use of GitHub Copilot within a Java-based monorepo containing multiple microservices.

## Getting Started
To get started with this project, follow these steps:

1. Clone the repository.
2. Install dependencies.
3. Run the application.

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

## Project Architecture
The project follows clean architecture principles with CQRS pattern implementation, as detailed in the `project.md` file. It uses an API-first design approach with OpenAPI specifications and event-driven architecture with Kafka.

## Features
- Monorepo structure with multiple services
- Clean architecture with CQRS
- API-first design using OpenAPI
- Event-driven architecture with Kafka
- Comprehensive testing strategy

## Contributing
Please read the [CONTRIBUTING.md](CONTRIBUTING.md) for details on our code of conduct and the process for submitting pull requests.

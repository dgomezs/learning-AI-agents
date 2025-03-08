# Example Prompts for GitHub Copilot

## General
- Generate a comprehensive README.md file for a new project.
- Create a shell script to automate development environment setup.
- Provide additional context to help GitHub Copilot generate more accurate code.

## Copilot Instructions

- Create a project.md file in the docs directory to establish code generation guidelines for Copilot.
- Structure this project as a monorepo with multiple services in the apps folder. Implement clean architecture for each service with clear separation of commands and queries. For testing, separate the Use Cases (containing application logic) from Adapters (handling external interactions). 
- Place both commands and queries within the use cases directory rather than creating separate folders for each.
- Implement Kafka for event-driven communication between services and follow API-first design with OpenAPI specifications.

## README Improvements

- Enhance the README file with detailed explanations of the docs folder structure and its contents.
- Update the README to describe this project as an e-commerce platform where users can search for products, add items to their shopping basket, and complete checkout using credit card payments.

## Business Logic Services

- Create a comprehensive specification document in the docs folder detailing the Product Catalog Service requirements and functionality.
- Create me a file under docs for the use cases of the product catalog service

## Project Setup

- Add appropriate entries to .gitignore for a Java project using Maven build system.


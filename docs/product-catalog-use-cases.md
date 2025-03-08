# Product Catalog Service Use Cases

This document outlines the detailed use cases for the Product Catalog Service in Gherkin format.

## Feature: Create New Product

```gherkin
Feature: Create New Product
  As a Product Manager
  I want to create new products in draft state
  So that I can prepare product information before making it publicly available

  Background:
    Given I am authenticated as a Product Manager
    And I have permission to manage products
    And the Categories  ["Sports", "Tennis"] exists
    And the Brand "SportMaster" exists

  Scenario: Successfully create a product with valid information
    When I submit a new product with the following details:
      | Field       | Value                        |
      | Name        | "Professional Tennis Racket" |
      | Description | "High-quality tennis racket" |
      | Brand       | "SportMaster"                |
      | SKU         | "TN-RCKT-001"                |
      | Categories  | ["Sports", "Tennis"]         |
    Then the system should create a new product in "draft" state
    And the system should return the newly created product ID
    And the system should return the product details
    And the system should emit a "ProductCreated" event
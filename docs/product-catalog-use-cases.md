# Product Catalog Service Use Cases

This document outlines the detailed use cases for the Product Catalog Service in Gherkin format.


## Feature: Create New Brand

```gherkin
Feature: Create New Brand
  As a Product Manager
  I want to create new brands in the system
  So that I can associate products with their manufacturers or trademark owners

  Background:
    Given I am authenticated as a Product Manager
    And I have permission to manage brands

  Scenario: Successfully create a brand with valid information
    When I submit a new brand with the following details:
      | Field       | Value                        |
      | Name        | "SportMaster"                |
      | Description | "Leading sports equipment manufacturer" |
      | Website     | "https://sportmaster.com"    |
      | Logo        | "sportmaster-logo.png"       |
    Then the system should create a new brand
    And the system should return the newly created brand ID
    And the system should return the brand details
    And the system should emit a "BrandCreated" event
package com.example.productcatalog.usecases;

import com.example.productcatalog.test.containers.PostgresTestContainer;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusIntegrationTest
@QuarkusTestResource(PostgresTestContainer.class)
public class CreateBrandIT {

    @Inject
    EntityManager entityManager;

    @Test
    @DisplayName("Should create brand via API and persist to database")
    public void shouldCreateBrandViaApiAndPersistToDatabase() {
        // Given
        String requestBody = """
            {
                "name": "Nike",
                "description": "Just Do It - Leading athletic wear and equipment",
                "website": "https://www.nike.com",
                "logoUrl": "https://www.nike.com/assets/logos/swoosh.png"
            }
            """;

        // When - Make API call to create brand
        var response = given()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/brands")
                .then()
                .statusCode(201)
                .contentType(ContentType.JSON)
                .body("name", equalTo("Nike"))
                .body("description", equalTo("Just Do It - Leading athletic wear and equipment"))
                .body("website", equalTo("https://www.nike.com"))
                .body("logoUrl", equalTo("https://www.nike.com/assets/logos/swoosh.png"))
                .body("id", notNullValue())
                .body("createdAt", notNullValue())
                .body("updatedAt", notNullValue())
                .extract();

        Long brandId = response.path("id");

        // Then - Verify brand was persisted to database
        verifyBrandExistsInDatabase(brandId, "Nike", "Just Do It - Leading athletic wear and equipment", 
            "https://www.nike.com", "https://www.nike.com/assets/logos/swoosh.png");
    }

    @Test
    @DisplayName("Should handle validation errors for invalid brand data")
    public void shouldHandleValidationErrorsForInvalidBrandData() {
        // Given - Invalid request with empty name
        String invalidRequestBody = """
            {
                "name": "",
                "description": "Some description",
                "website": "https://www.example.com",
                "logoUrl": "https://www.example.com/logo.png"
            }
            """;

        // When/Then - API should return validation error
        given()
                .contentType(ContentType.JSON)
                .body(invalidRequestBody)
                .when()
                .post("/brands")
                .then()
                .statusCode(400)
                .contentType("application/problem+json");
    }

    @Test
    @DisplayName("Should handle validation errors for invalid URL format")
    public void shouldHandleValidationErrorsForInvalidUrlFormat() {
        // Given - Invalid request with malformed URL
        String invalidRequestBody = """
            {
                "name": "TestBrand",
                "description": "Test description",
                "website": "not-a-valid-url",
                "logoUrl": "https://www.example.com/logo.png"
            }
            """;

        // When/Then - API should return validation error
        given()
                .contentType(ContentType.JSON)
                .body(invalidRequestBody)
                .when()
                .post("/brands")
                .then()
                .statusCode(400)
                .contentType("application/problem+json");
    }

    @Transactional
    private void verifyBrandExistsInDatabase(Long brandId, String expectedName, String expectedDescription, 
            String expectedWebsite, String expectedLogoUrl) {
        
        // Query the database directly to verify persistence
        var brand = entityManager.createQuery(
            "SELECT b FROM Brand b WHERE b.id = :id", 
            com.example.productcatalog.domain.model.Brand.class)
                .setParameter("id", brandId)
                .getSingleResult();

        assertNotNull(brand, "Brand should exist in database");
        assertEquals(expectedName, brand.getName(), "Brand name should match");
        assertEquals(expectedDescription, brand.getDescription(), "Brand description should match");
        assertEquals(expectedWebsite, brand.getWebsite().toString(), "Brand website should match");
        assertEquals(expectedLogoUrl, brand.getLogo().toString(), "Brand logo URL should match");
        assertNotNull(brand.getCreatedAt(), "Brand should have created timestamp");
        assertNotNull(brand.getUpdatedAt(), "Brand should have updated timestamp");
    }
}
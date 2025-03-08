package com.example.catalog.api.rest;

import com.example.catalog.api.rest.dto.CreateBrandRequest;
import com.example.catalog.api.rest.dto.BrandResponse;
import com.example.catalog.domain.model.Brand;
import com.example.catalog.infrastructure.persistence.BrandRepository;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import javax.inject.Inject;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
public class BrandResourceIT {

    @Inject
    BrandRepository brandRepository;

    @Test
    @DisplayName("Should create brand and persist to database")
    @TestSecurity(user = "product-manager", roles = {"product-manager"})
    public void shouldCreateAndPersistBrand() {
        // Arrange
        CreateBrandRequest request = new CreateBrandRequest(
                "SportMaster",
                "Leading sports equipment manufacturer",
                "https://sportmaster.com",
                "sportmaster-logo.png"
        );

        // Act & Assert API response
        BrandResponse response = given()
            .contentType(ContentType.JSON)
            .body(request)
            .when()
            .post("/api/v1/brands")
            .then()
            .statusCode(201)
            .body("id", notNullValue())
            .body("name", is("SportMaster"))
            .body("description", is("Leading sports equipment manufacturer"))
            .body("website", is("https://sportmaster.com"))
            .body("logoUrl", is("sportmaster-logo.png"))
            .extract()
            .as(BrandResponse.class);

        // Assert database persistence
        Optional<Brand> persistedBrand = brandRepository.findById(response.getId());
        
        assertTrue(persistedBrand.isPresent(), "Brand should be persisted in database");
        assertEquals("SportMaster", persistedBrand.get().getName(), "Persisted brand name should match");
        assertEquals("Leading sports equipment manufacturer", persistedBrand.get().getDescription(), "Persisted brand description should match");
        assertEquals("https://sportmaster.com", persistedBrand.get().getWebsite(), "Persisted brand website should match");
        assertEquals("sportmaster-logo.png", persistedBrand.get().getLogoUrl(), "Persisted brand logo URL should match");
    }
}

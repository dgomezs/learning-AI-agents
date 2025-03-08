package com.example.productcatalog.api.rest;

// Jakarta EE imports
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;

// Test framework imports
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusIntegrationTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.jupiter.api.Assertions.*;

// Application imports
import com.example.productcatalog.domain.model.Brand;
import com.example.productcatalog.api.rest.model.BrandResponse;
import com.example.productcatalog.api.rest.model.CreateBrandRequest;
import com.example.productcatalog.infrastructure.persistence.BrandRepository;
import com.example.productcatalog.test.containers.PostgresTestContainer;

import java.net.URI;

@QuarkusIntegrationTest
@QuarkusTestResource(PostgresTestContainer.class)
public class BrandResourceIT {

    @Inject
    BrandRepository brandRepository;
    

    @AfterEach
    void cleanup() {
        brandRepository.deleteAll();
    
    }

    @Nested
    @DisplayName("Create Brand")
    class CreateBrand {
        
        @Test
        @DisplayName("Should successfully create brand and emit event")
        void shouldCreateBrandSuccessfully() throws Exception{
            // Given
            CreateBrandRequest request = new CreateBrandRequest(
                    "SportMaster",
                    "Leading sports equipment manufacturer",
                    new URI("https://sportmaster.com"),
                    new URI("sportmaster-logo.png")
            );

            // When
            BrandResponse response = given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/v1/brands")
                .then()
                .statusCode(201)
                .contentType(MediaType.APPLICATION_JSON)
                .body("id", notNullValue())
                .body("name", is(request.getName()))
                .body("description", is(request.getDescription()))
                .body("website", is(request.getWebsite()))
                .body("logoUrl", is(request.getLogoUrl()))
                .extract()
                .as(BrandResponse.class);

            // Then
            // Verify database persistence
            Brand brand = brandRepository.findById(response.getId());
                        
            
            assertAll("Brand properties",
                () -> assertEquals(request.getName(), brand.getName(), "Brand name should match"),
                () -> assertEquals(request.getDescription(), brand.getDescription(), "Brand description should match"),
                () -> assertEquals(request.getWebsite(), brand.getWebsite(), "Brand website should match"),
                () -> assertEquals(request.getLogoUrl(), brand.getLogo(), "Brand logo URL should match")
            );

        
        }

    }
}
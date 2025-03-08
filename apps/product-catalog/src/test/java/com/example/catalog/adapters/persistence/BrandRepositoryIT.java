package com.example.catalog.adapters.persistence;

import com.example.catalog.domain.model.Brand;
import com.example.catalog.infrastructure.persistence.BrandRepository;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
public class BrandRepositoryIT {

    @Inject
    BrandRepository brandRepository;

    @Test
    @DisplayName("Should persist brand to database when created")
    @Transactional
    public void shouldPersistBrandToDatabase() {
        // Arrange
        Brand brand = Brand.builder()
                .name("SportMaster")
                .description("Leading sports equipment manufacturer")
                .website("https://sportmaster.com")
                .logoUrl("sportmaster-logo.png")
                .build();

        // Act
        Brand savedBrand = brandRepository.save(brand);
        
        // Assert
        assertNotNull(savedBrand.getId(), "Brand ID should not be null after saving");
        
        Optional<Brand> retrievedBrand = brandRepository.findById(savedBrand.getId());
        
        assertTrue(retrievedBrand.isPresent(), "Brand should be found in the database");
        assertEquals("SportMaster", retrievedBrand.get().getName(), "Brand name should match");
        assertEquals("Leading sports equipment manufacturer", retrievedBrand.get().getDescription(), "Brand description should match");
        assertEquals("https://sportmaster.com", retrievedBrand.get().getWebsite(), "Brand website should match");
        assertEquals("sportmaster-logo.png", retrievedBrand.get().getLogoUrl(), "Brand logo URL should match");
    }
}

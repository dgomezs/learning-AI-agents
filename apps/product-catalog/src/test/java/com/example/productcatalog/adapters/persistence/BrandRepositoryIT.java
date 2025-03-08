package com.example.productcatalog.adapters.persistence;

import com.example.productcatalog.domain.model.Brand;
import com.example.productcatalog.infrastructure.persistence.BrandRepository;
import io.quarkus.test.junit.QuarkusIntegrationTest;
import org.junit.jupiter.api.Test;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusIntegrationTest
@QuarkusTestResource(PostgresTestContainer.class)
public class BrandRepositoryIT {

    @Inject
    BrandRepository brandRepository;

    @Test
    @Transactional
    void shouldPersistAndFindBrand() {
        // Given
        Brand brand = new Brand();
        brand.setName("SportMaster");
        brand.setDescription("Leading sports equipment manufacturer");
        brand.setWebsite("https://sportmaster.com");
        brand.setLogo("sportmaster-logo.png");

        // When
        brandRepository.persist(brand);
        Optional<Brand> foundBrand = brandRepository.findByIdOptional(brand.getId());

        // Then
        assertTrue(foundBrand.isPresent());
        assertEquals("SportMaster", foundBrand.get().getName());
        assertEquals("Leading sports equipment manufacturer", foundBrand.get().getDescription());
        assertEquals("https://sportmaster.com", foundBrand.get().getWebsite());
        assertEquals("sportmaster-logo.png", foundBrand.get().getLogo());
    }
}

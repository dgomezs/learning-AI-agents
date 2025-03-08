package com.example.productcatalog.adapters.persistence;

import com.example.productcatalog.domain.model.Brand;
import com.example.productcatalog.infrastructure.persistence.BrandRepository;
import com.example.productcatalog.test.containers.PostgresTestContainer;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusIntegrationTest;
import org.junit.jupiter.api.Test;
import javax.inject.Inject;

import java.net.URI;
import java.net.URISyntaxException;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusIntegrationTest
@QuarkusTestResource(PostgresTestContainer.class)
public class BrandRepositoryIT {

    @Inject
    BrandRepository brandRepository;

    @Test

    void shouldPersistAndFindBrand() throws URISyntaxException {
        // Given
        Brand brand = new Brand();
        brand.setName("SportMaster");
        brand.setDescription("Leading sports equipment manufacturer");
        brand.setWebsite(new URI("https://sportmaster.com"));
        brand.setLogo(  new URI("sportmaster-logo.png"));

        // When
        brandRepository.persist(brand);
        Brand foundBrand = brandRepository.findById(brand.getId());

        // Then
        
        assertEquals("SportMaster", foundBrand.getName());
        assertEquals("Leading sports equipment manufacturer", foundBrand.getDescription());
        assertEquals("https://sportmaster.com", foundBrand.getWebsite());
        assertEquals("sportmaster-logo.png", foundBrand.getLogo());
    }
}

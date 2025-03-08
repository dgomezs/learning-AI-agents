package com.example.productcatalog.usecases;

import com.example.productcatalog.domain.model.Brand;
import com.example.productcatalog.domain.services.BrandService;
import com.example.productcatalog.application.usecases.CreateBrandCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateBrandCommandTest {

    private BrandService brandService;
    private CreateBrandCommand createBrandCommand;

    @BeforeEach
    void setUp() {
        brandService = Mockito.mock(BrandService.class);
        createBrandCommand = new CreateBrandCommand(brandService);
    }

    @Test
    void shouldCreateBrandWithValidInformation() {
        // Given
        Brand brand = new Brand("SportMaster", "Leading sports equipment manufacturer", "https://sportmaster.com", "sportmaster-logo.png");
        when(brandService.createBrand(any(Brand.class))).thenReturn(brand);

        // When
        Brand createdBrand = createBrandCommand.execute(brand);

        // Then
        assertNotNull(createdBrand);
        assertEquals("SportMaster", createdBrand.getName());
        assertEquals("Leading sports equipment manufacturer", createdBrand.getDescription());
        assertEquals("https://sportmaster.com", createdBrand.getWebsite());
        assertEquals("sportmaster-logo.png", createdBrand.getLogo());
        verify(brandService, times(1)).createBrand(any(Brand.class));
    }
}

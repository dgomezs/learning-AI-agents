package com.example.productcatalog.usecases;

import com.example.productcatalog.domain.model.Brand;
import com.example.productcatalog.infrastructure.events.EventPublisher;
import com.example.productcatalog.infrastructure.persistence.BrandRepository;
import com.google.inject.Inject;

import io.quarkus.test.junit.QuarkusTest;

import com.example.productcatalog.domain.events.BrandCreatedEvent;

import com.example.productcatalog.application.usecases.CreateBrandCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.net.URI;
import java.net.URISyntaxException;

//@QuarkusTest
class CreateBrandCommandTest {

    private static final Long BRAND_ID = 1L;

    private BrandRepository brandRepository;

    private EventPublisher eventPublisher;
    private CreateBrandCommand createBrandCommand;

    @BeforeEach
    void setUp() {
        brandRepository = Mockito.mock(BrandRepository.class);
        eventPublisher = Mockito.mock(EventPublisher.class);

        createBrandCommand = new CreateBrandCommand(brandRepository, eventPublisher);
    }

    @Test
    void shouldCreateBrand() throws URISyntaxException {
        // Given
        CreateBrandCommand.Input input = new CreateBrandCommand.Input(
                "SportMaster",
                "Leading sports equipment manufacturer",
                new URI("https://sportmaster.com"),
                new URI("sportmaster-logo.png"));

        Brand expectedBrand = Brand.builder()
                .id(BRAND_ID)
                .name(input.getName())
                .description(input.getDescription())
                .website(input.getWebsite())
                .logo(input.getLogoUrl())
                .build();

        // Mocking the behavior of the brandRepository to return the expectedBrand when
        // findById is called with any value
        when(brandRepository.findById(any())).thenReturn(expectedBrand);

        // When
        CreateBrandCommand.Output output = createBrandCommand.execute(input);

        // Then
        assertNotNull(output);
        assertEquals(expectedBrand.getId(), output.getId());
        assertEquals(expectedBrand.getName(), output.getName());
        assertEquals(expectedBrand.getDescription(), output.getDescription());
        assertEquals(expectedBrand.getWebsite(), output.getWebsite());
        assertEquals(expectedBrand.getLogo(), output.getLogoUrl());

        verify(brandRepository, times(1)).persist(argThat((Brand brand) -> brand.getName().equals(input.getName()) &&
                brand.getDescription().equals(input.getDescription()) &&
                brand.getWebsite().equals(input.getWebsite()) &&
                brand.getLogo().equals(input.getLogoUrl())));

        verify(eventPublisher, times(1)).publish(argThat((BrandCreatedEvent event) -> event.getId().equals(BRAND_ID) &&
                event.getName().equals(expectedBrand.getName()) &&
                event.getDescription().equals(expectedBrand.getDescription()) &&
                event.getWebsite().equals(expectedBrand.getWebsite().toString()) &&
                event.getLogoUrl().equals(expectedBrand.getLogo().toString())));
    }

}

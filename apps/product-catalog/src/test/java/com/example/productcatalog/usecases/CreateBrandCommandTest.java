package com.example.productcatalog.usecases;

import com.example.productcatalog.domain.model.Brand;
import com.example.productcatalog.infrastructure.events.EventPublisher;
import com.example.productcatalog.infrastructure.persistence.BrandRepository;


import com.example.productcatalog.application.usecases.CreateBrandCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.net.URI;
import java.net.URISyntaxException;



class CreateBrandCommandTest {

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
    void shouldCreateBrand() throws URISyntaxException{
        // Given
        CreateBrandCommand.Input input = new CreateBrandCommand.Input(
            "SportMaster",
            "Leading sports equipment manufacturer",
            new URI("https://sportmaster.com"),
            new URI("sportmaster-logo.png")
        );

        Brand brand = Brand.builder()
            .name(input.getName())
            .description(input.getDescription())
            .website(input.getWebsite())
            .logo(input.getLogoUrl())
            .build();

    



        // When
        CreateBrandCommand.Output output = createBrandCommand.execute(input);

        // Then
        assertNotNull(output);
        assertEquals(brand.getId(), output.getId());
        assertEquals(brand.getName(), output.getName());
        assertEquals(brand.getDescription(), output.getDescription());
        assertEquals(brand.getWebsite(), output.getWebsite());
        assertEquals(brand.getLogo(), output.getLogoUrl());

        verify(brandRepository, times(1)).persist(brand);
        verify(eventPublisher, times(1)).publish(any());
    }
    

}

package com.example.catalog.application.usecases;

import com.example.catalog.domain.model.Brand;
import com.example.catalog.domain.events.BrandCreatedEvent;
import com.example.catalog.infrastructure.persistence.BrandRepository;
import com.example.catalog.infrastructure.events.EventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateBrandCommandTest {

    @Mock
    private BrandRepository brandRepository;
    
    @Mock
    private EventPublisher eventPublisher;
    
    private CreateBrandCommand createBrandCommand;

    @BeforeEach
    void setUp() {
        createBrandCommand = new CreateBrandCommand(brandRepository, eventPublisher);
    }

    @Test
    @DisplayName("Should create brand and publish event when valid data is provided")
    void shouldCreateBrandAndPublishEvent() {
        // Arrange
        CreateBrandCommand.Input input = new CreateBrandCommand.Input(
                "SportMaster", 
                "Leading sports equipment manufacturer",
                "https://sportmaster.com", 
                "sportmaster-logo.png"
        );
        
        Brand savedBrand = Brand.builder()
                .id(1L)
                .name("SportMaster")
                .description("Leading sports equipment manufacturer")
                .website("https://sportmaster.com")
                .logoUrl("sportmaster-logo.png")
                .build();
        
        when(brandRepository.save(any(Brand.class))).thenReturn(savedBrand);
        
        // Act
        CreateBrandCommand.Output output = createBrandCommand.execute(input);
        
        // Assert
        assertEquals(1L, output.getId(), "Should return the correct brand ID");
        
        // Verify brand was saved to repository
        ArgumentCaptor<Brand> brandCaptor = ArgumentCaptor.forClass(Brand.class);
        verify(brandRepository).save(brandCaptor.capture());
        
        Brand capturedBrand = brandCaptor.getValue();
        assertEquals("SportMaster", capturedBrand.getName(), "Brand name should match");
        assertEquals("Leading sports equipment manufacturer", capturedBrand.getDescription(), "Brand description should match");
        
        // Verify event was published
        ArgumentCaptor<BrandCreatedEvent> eventCaptor = ArgumentCaptor.forClass(BrandCreatedEvent.class);
        verify(eventPublisher).publish(eventCaptor.capture());
        
        BrandCreatedEvent capturedEvent = eventCaptor.getValue();
        assertEquals(1L, capturedEvent.getBrandId(), "Event should contain correct brand ID");
        assertEquals("SportMaster", capturedEvent.getName(), "Event should contain correct brand name");
    }
}

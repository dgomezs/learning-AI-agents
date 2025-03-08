package com.example.productcatalog.application.usecases;

import com.example.productcatalog.domain.model.Brand;
import com.example.productcatalog.domain.events.BrandCreatedEvent;
import com.example.productcatalog.infrastructure.persistence.BrandRepository;
import com.example.productcatalog.infrastructure.events.EventPublisher;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.time.OffsetDateTime;

/**
 * Use case for creating a new brand.
 */
@ApplicationScoped
@RequiredArgsConstructor
public class CreateBrandCommand {
    
    private final BrandRepository brandRepository;
    private final EventPublisher eventPublisher;
    
    /**
     * Execute the command to create a new brand.
     *
     * @param input the command input data
     * @return the output with the new brand ID and details
     */
    @Transactional
    public Output execute(Input input) {
        Brand brand = Brand.builder()
                .name(input.getName())
                .description(input.getDescription())
                .website(input.getWebsite())
                .logoUrl(input.getLogoUrl())
                .build();
        
        Brand savedBrand = brandRepository.save(brand);
        
        BrandCreatedEvent event = BrandCreatedEvent.builder()
                .brandId(savedBrand.getId())
                .name(savedBrand.getName())
                .description(savedBrand.getDescription())
                .website(savedBrand.getWebsite())
                .logoUrl(savedBrand.getLogoUrl())
                .build();
        
        eventPublisher.publish(event);
        
        return new Output(
            savedBrand.getId(),
            savedBrand.getName(),
            savedBrand.getDescription(),
            savedBrand.getWebsite(),
            savedBrand.getLogoUrl(),
            savedBrand.getCreatedAt(),
            savedBrand.getUpdatedAt()
        );
    }
    
    /**
     * Input data for creating a brand.
     */
    @Data
    @AllArgsConstructor
    public static class Input {
        private String name;
        private String description;
        private String website;
        private String logoUrl;
    }
    
    /**
     * Output data after creating a brand.
     */
    @Data
    @AllArgsConstructor
    public static class Output {
        private Long id;
        private String name;
        private String description;
        private String website;
        private String logoUrl;
        private OffsetDateTime createdAt;
        private OffsetDateTime updatedAt;
    }
}
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

import java.net.URI;
import java.time.Instant;
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
                .logo(input.getLogoUrl())
                .build();
        
        // Using Quarkus Panache repository method for persistence
        brandRepository.persist(brand);
        
        BrandCreatedEvent event = BrandCreatedEvent.builder()
                .brandId(brand.getId())
                .name(brand.getName())
                .description(brand.getDescription())
                .website(brand.getWebsite().toString())
                .logoUrl(brand.getLogo().toString())
                .build();
        
        eventPublisher.publish(event);
        
        return new Output(
            brand.getId(),
            brand.getName(),
            brand.getDescription(),
            brand.getWebsite(),
            brand.getLogo(),
            brand.getCreatedAt(),
            brand.getUpdatedAt()
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
        private URI website;
        private URI logoUrl;
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
        private URI website;
        private URI logoUrl;
        private Instant createdAt;
        private Instant updatedAt;
    }
}
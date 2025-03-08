package com.example.productcatalog.application.usecases;

import com.example.productcatalog.domain.model.Brand;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

/**
 * Command to create a new brand in the system.
 */
@ApplicationScoped
@Slf4j
public class CreateBrandCommand {

    /**
     * Request data for creating a brand
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotBlank(message = "Brand name is required")
        @Size(max = 100, message = "Brand name cannot exceed 100 characters")
        private String name;

        @Size(max = 500, message = "Description cannot exceed 500 characters")
        private String description;

        @Size(max = 255, message = "Website URL cannot exceed 255 characters")
        private String website;

        @Size(max = 255, message = "Logo URL cannot exceed 255 characters")
        private String logoUrl;
    }

    /**
     * Response data for brand creation
     */
    @Data
    @Builder
    @AllArgsConstructor
    public static class Response {
        private UUID id;
        private String name;
        private String description;
        private String website;
        private String logoUrl;

        public Response(Brand brand) {
            this.id = brand.getId();
            this.name = brand.getName();
            this.description = brand.getDescription();
            this.website = brand.getWebsite();
            this.logoUrl = brand.getLogoUrl();
        }
    }

    /**
     * Execute the command to create a brand
     * @param request The brand creation request
     * @return Response with the created brand details
     */
    public Response execute(@Valid Request request) {
        log.info("Creating brand with name: {}", request.getName());
        
        // Create a new brand entity
        Brand brand = new Brand(request.getName());
        brand.setDescription(request.getDescription());
        brand.setWebsite(request.getWebsite());
        brand.setLogoUrl(request.getLogoUrl());
        
        // Here we would typically save the brand via a repository
        // and publish events, but this is a skeleton implementation
        log.info("Brand created with ID: {}", brand.getId());
        
        return new Response(brand);
    }
}

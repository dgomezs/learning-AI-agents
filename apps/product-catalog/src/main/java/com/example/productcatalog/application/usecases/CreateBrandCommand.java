package com.example.productcatalog.application.usecases;

import com.example.productcatalog.domain.model.Brand;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.UUID;

/**
 * Command to create a new brand in the system.
 */
@ApplicationScoped
public class CreateBrandCommand {

    /**
     * Request data for creating a brand
     */
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

        // Getters and setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getWebsite() {
            return website;
        }

        public void setWebsite(String website) {
            this.website = website;
        }

        public String getLogoUrl() {
            return logoUrl;
        }

        public void setLogoUrl(String logoUrl) {
            this.logoUrl = logoUrl;
        }
    }

    /**
     * Response data for brand creation
     */
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

        // Getters
        public UUID getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public String getWebsite() {
            return website;
        }

        public String getLogoUrl() {
            return logoUrl;
        }
    }

    /**
     * Execute the command to create a brand
     * @param request The brand creation request
     * @return Response with the created brand details
     */
    public Response execute(@Valid Request request) {
        // Create a new brand entity
        Brand brand = new Brand(request.getName());
        brand.setDescription(request.getDescription());
        brand.setWebsite(request.getWebsite());
        brand.setLogoUrl(request.getLogoUrl());
        
        // Here we would typically save the brand via a repository
        // and publish events, but this is a skeleton implementation
        
        return new Response(brand);
    }
}

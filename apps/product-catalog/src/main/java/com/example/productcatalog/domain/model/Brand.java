package com.example.productcatalog.domain.model;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Domain model representing a product brand.
 */
public class Brand {
    private UUID id;
    private String name;
    private String description;
    private String website;
    private String logoUrl;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    // Default constructor
    public Brand() {
    }

    // Constructor with required fields
    public Brand(String name) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = OffsetDateTime.now();
    }

    // Full constructor
    public Brand(UUID id, String name, String description, String website, String logoUrl, 
                 OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.website = website;
        this.logoUrl = logoUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Update the brand details and set updated timestamp
    public void update(String name, String description, String website, String logoUrl) {
        this.name = name;
        this.description = description;
        this.website = website;
        this.logoUrl = logoUrl;
        this.updatedAt = OffsetDateTime.now();
    }
}

package com.example.productcatalog.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Domain model representing a product brand.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Brand {
    private UUID id;
    private String name;
    private String description;
    private String website;
    private String logoUrl;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    /**
     * Constructor with required fields, auto-generates id and timestamps
     * 
     * @param name The name of the brand
     */
    public Brand(String name) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = OffsetDateTime.now();
    }

    /**
     * Update the brand details and set updated timestamp
     * 
     * @param name The updated name
     * @param description The updated description
     * @param website The updated website
     * @param logoUrl The updated logo URL
     */
    public void update(String name, String description, String website, String logoUrl) {
        this.name = name;
        this.description = description;
        this.website = website;
        this.logoUrl = logoUrl;
        this.updatedAt = OffsetDateTime.now();
    }
}

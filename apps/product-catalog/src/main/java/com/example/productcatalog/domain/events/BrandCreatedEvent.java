package com.example.productcatalog.domain.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

/**
 * Event emitted when a new brand is created.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BrandCreatedEvent {
    private Long id;
    private String name;
    private String description;
    private String website;
    private String logoUrl;
    

}
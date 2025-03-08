package com.example.productcatalog.api.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BrandResponse {
    private Long id;
    private String name;
    private String description;
    private String website;
    private String logoUrl;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
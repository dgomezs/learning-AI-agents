package com.example.productcatalog.api.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateBrandRequest {
    @NotBlank(message = "Brand name is required")
    @Size(max = 100, message = "Brand name must be less than 100 characters")
    private String name;
    
    private String description;
    
    @Size(max = 255, message = "Website URL must be less than 255 characters")
    private String website;
    
    @Size(max = 255, message = "Logo URL must be less than 255 characters")
    private String logoUrl;
}
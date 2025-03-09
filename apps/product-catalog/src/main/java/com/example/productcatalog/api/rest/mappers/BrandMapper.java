package com.example.productcatalog.api.rest.mappers;


import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import com.example.productcatalog.api.rest.model.BrandResponse;
import com.example.productcatalog.api.rest.model.CreateBrandRequest;
import com.example.productcatalog.application.usecases.CreateBrandCommand;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.NonNull;

@ApplicationScoped
public class BrandMapper {

    /**
     * Maps API request to command input.
     *
     * @param request the create brand request
     * @return command input object
     * @throws IllegalArgumentException if request is null
     */
    public CreateBrandCommand.Input toCommandInput(@NonNull CreateBrandRequest request) {
        return new CreateBrandCommand.Input(
                request.getName(),
                request.getDescription(),
                request.getWebsite(),
                request.getLogoUrl());
    }

    /**
     * Maps command output to API response.
     *
     * @param output the command output
     * @return brand response object
     * @throws IllegalArgumentException if output is null
     */
    public BrandResponse toResponse(@NonNull CreateBrandCommand.Output output) {
        return new BrandResponse()
                .id(output.getId())
                .name(output.getName())
                .description(output.getDescription())
                .website(output.getWebsite())
                .logoUrl(output.getLogoUrl())
                .createdAt(formatDateTimeUTC(output.getCreatedAt()))
                .updatedAt(formatDateTimeUTC(output.getUpdatedAt())); 
    }

    /**
     * Converts Instant to OffsetDateTime in UTC.
     *
     * @param instant the instant to convert
     * @return OffsetDateTime in UTC, or null if input is null
     */
    private OffsetDateTime formatDateTimeUTC(Instant instant) {
        return instant != null ? OffsetDateTime.ofInstant(instant, ZoneOffset.UTC) : null;
    }
}

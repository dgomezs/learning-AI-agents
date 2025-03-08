package com.example.productcatalog.api.rest;

import com.example.productcatalog.api.rest.dto.BrandResponse;
import com.example.productcatalog.api.rest.dto.CreateBrandRequest;
import com.example.productcatalog.application.usecases.CreateBrandCommand;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import lombok.RequiredArgsConstructor;

@Path("/api/v1/brands")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
@RequiredArgsConstructor
public class BrandResource {

    private final CreateBrandCommand createBrandCommand;

    @POST
    @RolesAllowed("product-manager")
    public Response createBrand(@Valid CreateBrandRequest request) {
        CreateBrandCommand.Input input = new CreateBrandCommand.Input(
                request.getName(),
                request.getDescription(),
                request.getWebsite(),
                request.getLogoUrl()
        );
        
        CreateBrandCommand.Output output = createBrandCommand.execute(input);
        
        BrandResponse response = BrandResponse.builder()
                .id(output.getId())
                .name(output.getName())
                .description(output.getDescription())
                .website(output.getWebsite())
                .logoUrl(output.getLogoUrl())
                .createdAt(output.getCreatedAt())
                .updatedAt(output.getUpdatedAt())
                .build();
                
        return Response
                .created(UriBuilder.fromResource(BrandResource.class).path(String.valueOf(response.getId())).build())
                .entity(response)
                .build();
    }
}
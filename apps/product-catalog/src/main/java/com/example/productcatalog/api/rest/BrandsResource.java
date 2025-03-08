package com.example.productcatalog.api.rest;

import com.example.productcatalog.api.rest.model.BrandResponse;
import com.example.productcatalog.api.rest.model.CreateBrandRequest;
import com.example.productcatalog.application.usecases.CreateBrandCommand;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import com.example.productcatalog.api.rest.mappers.BrandMapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
public class BrandsResource implements BrandsApi {

    private final CreateBrandCommand createBrandCommand;
    private final BrandMapper brandMapper;

    @Override
    public CompletionStage<Response> createBrand(CreateBrandRequest request) {
        log.info("Received request to create brand: {}", request.getName());
        
        CreateBrandCommand.Input commandInput = brandMapper.toCommandInput(request);
        CreateBrandCommand.Output output = createBrandCommand.execute(commandInput);
        BrandResponse response = brandMapper.toResponse(output);
        
        return CompletableFuture.supplyAsync(() -> Response.status(Response.Status.CREATED)
                      .entity(response)
                      .build());
    }
}

package com.example.productcatalog.application.usecases;

import com.example.productcatalog.domain.events.BrandCreatedEvent;
import com.example.productcatalog.domain.model.Brand;
import com.example.productcatalog.infrastructure.events.EventPublisher;
import com.example.productcatalog.infrastructure.persistence.BrandRepository;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.net.URI;


@RequiredArgsConstructor
public class CreateBrandCommand {
    private final BrandRepository brandRepository;
    private final EventPublisher eventPublisher;

    public Output execute(Input input) {
        Brand brand = Brand.builder()
                .name(input.getName())
                .description(input.getDescription())
                .website(input.getWebsite())
                .logo(input.getLogoUrl())
                .build();

        Brand savedBrand = brandRepository.persist(brand);

        eventPublisher.publish(new BrandCreatedEvent(
                savedBrand.getId(),
                savedBrand.getName(),
                savedBrand.getDescription(),
                savedBrand.getWebsite().toString(),
                savedBrand.getLogo().toString()
        ));

        return new Output(
                savedBrand.getId(),
                savedBrand.getName(),
                savedBrand.getDescription(),
                savedBrand.getWebsite(),
                savedBrand.getLogo()
        );
    }

    @Value
    public static class Input {
        String name;
        String description;
        URI website;
        URI logoUrl;
    }

    @Value
    public static class Output {
        Long id;
        String name;
        String description;
        URI website;
        URI logoUrl;
    }
}
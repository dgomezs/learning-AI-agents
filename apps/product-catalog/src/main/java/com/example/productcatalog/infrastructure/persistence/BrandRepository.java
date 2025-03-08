package com.example.productcatalog.infrastructure.persistence;

import com.example.productcatalog.domain.model.Brand;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public interface BrandRepository extends PanacheRepository<Brand> {
    
}
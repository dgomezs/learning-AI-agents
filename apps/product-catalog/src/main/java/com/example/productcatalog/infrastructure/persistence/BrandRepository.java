package com.example.productcatalog.infrastructure.persistence;

import com.example.productcatalog.domain.model.Brand;

public interface BrandRepository {
    Brand persist(Brand brand);
}
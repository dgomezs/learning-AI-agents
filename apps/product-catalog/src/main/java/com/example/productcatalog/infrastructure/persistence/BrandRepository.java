package com.example.productcatalog.infrastructure.persistence;

import com.example.productcatalog.domain.model.Brand;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class BrandRepository implements PanacheRepository<Brand> {
    // Define any custom query methods here
}
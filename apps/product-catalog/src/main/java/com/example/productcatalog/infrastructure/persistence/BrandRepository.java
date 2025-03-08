package com.example.productcatalog.infrastructure.persistence;

import com.example.productcatalog.domain.model.Brand;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.Optional;
import java.time.OffsetDateTime;

/**
 * Repository for managing Brand entities.
 */
@ApplicationScoped
public class BrandRepository {

    @PersistenceContext
    EntityManager em;

    /**
     * Save a brand entity.
     *
     * @param brand the brand to save
     * @return the saved brand
     */
    @Transactional
    public Brand save(Brand brand) {
        if (brand.getId() == null) {
            em.persist(brand);
            return brand;
        } else {
            brand.setUpdatedAt(OffsetDateTime.now());
            return em.merge(brand);
        }
    }

    /**
     * Find a brand by ID.
     *
     * @param id the brand ID
     * @return an Optional containing the brand, or empty if not found
     */
    public Optional<Brand> findById(Long id) {
        Brand brand = em.find(Brand.class, id);
        return Optional.ofNullable(brand);
    }

    /**
     * Find a brand by name.
     *
     * @param name the brand name
     * @return an Optional containing the brand, or empty if not found
     */
    public Optional<Brand> findByName(String name) {
        try {
            Brand brand = em.createQuery("SELECT b FROM Brand b WHERE b.name = :name", Brand.class)
                    .setParameter("name", name)
                    .getSingleResult();
            return Optional.of(brand);
        } catch (jakarta.persistence.NoResultException e) {
            return Optional.empty();
        }
    }

    /**
     * Delete a brand.
     *
     * @param brand the brand to delete
     */
    @Transactional
    public void delete(Brand brand) {
        if (em.contains(brand)) {
            em.remove(brand);
        } else {
            em.remove(em.merge(brand));
        }
    }
}
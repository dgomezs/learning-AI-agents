package com.example.ecommerce.shared.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Base entity class for all domain entities.
 * 
 * @param <ID> Type of the entity identifier
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
public abstract class Entity<ID extends Serializable> implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private ID id;
    private Long version;
    
    /**
     * Checks if the entity is new (not persisted yet).
     *
     * @return true if the entity is new, false otherwise
     */
    public boolean isNew() {
        return id == null;
    }
}

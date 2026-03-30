package edu.campus.ccrm.domain.entities;

import edu.campus.ccrm.domain.interfaces.Displayable;
import edu.campus.ccrm.domain.interfaces.Validatable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Abstract base class for all entities in the system.
 * Implements common functionality and demonstrates inheritance and
 * polymorphism.
 */
public abstract class AbstractEntity implements Displayable, Validatable {
    protected final String id;
    protected final LocalDateTime createdAt;
    protected final LocalDateTime updatedAt;

    protected AbstractEntity(String id) {
        this.id = Objects.requireNonNull(id, "ID cannot be null");
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    protected AbstractEntity(String id, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = Objects.requireNonNull(id, "ID cannot be null");
        this.createdAt = Objects.requireNonNull(createdAt, "Created date cannot be null");
        this.updatedAt = Objects.requireNonNull(updatedAt, "Updated date cannot be null");
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public boolean isValid() {
        return id != null && !id.trim().isEmpty();
    }

    @Override
    public List<String> getValidationErrors() {
        List<String> errors = new ArrayList<>();
        if (id == null || id.trim().isEmpty()) {
            errors.add("ID is required");
        }
        return errors;
    }

    @Override
    public String getSummary() {
        return String.format("%s [ID: %s]", getClass().getSimpleName(), id);
    }

    @Override
    public String toDisplayString() {
        return getSummary();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        AbstractEntity that = (AbstractEntity) obj;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return getSummary();
    }
}

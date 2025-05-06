package com.admin.catalogo.infrastructure.persistence;

import com.admin.catalogo.domain.category.Category;
import com.admin.catalogo.domain.category.CategoryID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "category")
public class CategoryJpaEntity {

    @Id
    private String id;

    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Column(name = "description", length = 4000)
    private String description;

    @Column(name = "active", nullable = false)
    private Boolean active;

    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant createdAt;

    public CategoryJpaEntity() {
    }

    private CategoryJpaEntity(String id, String name, String description, Boolean isActive, Instant createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.active = isActive;
        this.createdAt = createdAt;
    }

    public static CategoryJpaEntity from(final Category aCategory) {
        return new CategoryJpaEntity(
                aCategory.getId().getValue(),
                aCategory.getName(),
                aCategory.getDescription(),
                aCategory.isActive(),
                aCategory.getCreatedAt()
        );
    }

    public Category toAggregate() {
        return Category.with(
                CategoryID.from(this.id),
                this.name,
                this.description,
                this.active,
                this.createdAt
        );
    }



    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant updatedAt;

    @Column(name = "deleted_at",  columnDefinition = "DATETIME(6)")
    private Instant deletedAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }
}

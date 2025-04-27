package com.admin.catalogo.domain.category;

import com.admin.catalogo.domain.AggregateRoot;
import com.admin.catalogo.domain.validation.ValidationHandler;

import java.time.Instant;
import java.util.UUID;

public class Category extends AggregateRoot<CategoryID> implements Cloneable {

        private String name;
        private String description;
        private boolean active;
        private Instant createdAt;
        private Instant updatedAt;
        private Instant deletedAt;

        private Category(final CategoryID id,
                         final String name,
                         final String description,
                         final boolean active,
                         final Instant updatedAt,
                         final Instant createdAt,
                         final Instant deletedAt) {
                super(id);

                this.name = name;
                this.description = description;
                this.active = active;
                this.updatedAt = updatedAt;
                this.createdAt = createdAt;
                this.deletedAt = deletedAt;
        }

        @Override
        public void validate(final ValidationHandler handler) {
                new CategoryValidator(this, handler).Validate();
        }


        @Override
        public CategoryID getId() {
                return id;
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

        public boolean isActive() {
                return active;
        }

        public void setActive(boolean active) {
                this.active = active;
        }

        public Instant getCreatedAt() {
                return createdAt;
        }

        public void setCreatedAt(Instant createdAt) {
                this.createdAt = createdAt;
        }

        public Instant getUpdatedAt() {
                return updatedAt;
        }

        public void setUpdatedAt(Instant updatedAt) {
                this.updatedAt = updatedAt;
        }

        public Instant getDeletedAt() {
                return deletedAt;
        }

        public void setDeletedAt(Instant deletedAt) {
                this.deletedAt = deletedAt;
        }

        public Category deactivate() {
                if(getDeletedAt() == null){
                        this.deletedAt = Instant.now();
                }
                this.setActive(false);
                this.setUpdatedAt(Instant.now());
                return this;
        }

        public Category activate() {
                if(getDeletedAt() != null){
                        this.deletedAt = null;
                }
                this.setActive(true);
                this.setUpdatedAt(Instant.now());
                return this;
        }

        public Category update(String name, String description, boolean active) {
                this.name = name;
                this.description = description;
                this.active = active;
                this.setUpdatedAt(Instant.now());
                return this;
        }


        public static Category newCategory(
                                           final String name,
                                           final String description,
                                           final boolean active){
                final var id = CategoryID.unique();
                final var now = Instant.now();

                return new Category(id, name, description, active, now, now, null);

        }

    @Override
    public Category clone() {
        try {
            Category clone = (Category) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
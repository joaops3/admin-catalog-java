package com.admin.catalogo.infrastructure.category.presenters;

import com.admin.catalogo.application.category.retrieve.get.CategoryOutput;
import com.admin.catalogo.infrastructure.category.models.CategoryApiOutput;

import java.util.function.Function;

public interface CategoryApiPresenter {

    static CategoryOutput present(final CategoryOutput output) {
        return new CategoryOutput(
                output.id(),
                output.name(),
                output.description(),
                output.isActive(),
                output.createdAt(),
                output.updatedAt(),
                output.deletedAt()
        );
    }

    Function<CategoryOutput, CategoryApiOutput> present = output -> new CategoryApiOutput(
            output.id().getValue(),
            output.name(),
            output.description(),
            output.isActive(),
            output.createdAt(),
            output.updatedAt(),
            output.deletedAt()
    );
}

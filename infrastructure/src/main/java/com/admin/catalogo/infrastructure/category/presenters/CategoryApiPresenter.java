package com.admin.catalogo.infrastructure.category.presenters;

import com.admin.catalogo.application.category.retrieve.get.CategoryOutput;
import com.admin.catalogo.application.category.retrieve.list.CategoryListOutput;
import com.admin.catalogo.infrastructure.category.models.CategoryApiResponse;
import com.admin.catalogo.infrastructure.category.models.CategoryListResponse;

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

    Function<CategoryOutput, CategoryApiResponse> present = output -> new CategoryApiResponse(
            output.id().getValue(),
            output.name(),
            output.description(),
            output.isActive(),
            output.createdAt(),
            output.updatedAt(),
            output.deletedAt()
    );

    static CategoryListResponse present(final CategoryListOutput output) {
        return new CategoryListResponse(
                output.id().getValue(),
                output.name(),
                output.description(),
                output.isActive(),
                output.createdAt()

        );
    }
}

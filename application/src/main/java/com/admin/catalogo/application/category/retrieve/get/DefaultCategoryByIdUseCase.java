package com.admin.catalogo.application.category.retrieve.get;

import com.admin.catalogo.domain.category.Category;
import com.admin.catalogo.domain.category.CategoryGateway;
import com.admin.catalogo.domain.category.CategoryID;
import com.admin.catalogo.domain.exceptions.NotFoundException;

public class DefaultCategoryByIdUseCase extends GetCategoryByIdUseCase {

    private final CategoryGateway categoryGateway;

    public DefaultCategoryByIdUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }

    @Override
    public CategoryOutput execute(final String anInput) {
        return this.categoryGateway.findById(CategoryID.from(anInput))
                .map(CategoryOutput::from)
                .orElseThrow(() -> NotFoundException.with(Category.class, CategoryID.from(anInput)));
    }
}

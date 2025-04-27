package com.admin.catalogo.application.category.retrieve.list;

import com.admin.catalogo.domain.category.CategoryGateway;
import com.admin.catalogo.domain.category.CategorySearchQuery;
import com.admin.catalogo.domain.pagination.Pagination;

public class DefaultCategoryListUseCase extends ListCategoriesUseCase {

    private final CategoryGateway categoryGateway;

    public DefaultCategoryListUseCase(CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }

    @Override
    public Pagination<CategoryListOutput> execute(CategorySearchQuery input) {
        return this.categoryGateway.findAll(input)
                .map(CategoryListOutput::from);
    }
}

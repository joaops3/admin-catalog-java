package com.admin.catalogo.application.category.create;

import com.admin.catalogo.domain.category.Category;
import com.admin.catalogo.domain.category.CategoryID;

public record CreateCategoryOutput(CategoryID id) {


    public static CreateCategoryOutput from(final Category category) {
        return new CreateCategoryOutput(category.getId());
    }

    public static CreateCategoryOutput from(final CategoryID categoryId) {
        return new CreateCategoryOutput(categoryId);
    }
}

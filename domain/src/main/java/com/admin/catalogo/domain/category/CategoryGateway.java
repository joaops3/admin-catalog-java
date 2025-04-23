package com.admin.catalogo.domain.category;

import com.admin.catalogo.domain.pagination.Pagination;

import java.util.Optional;

public interface CategoryGateway {
    Category create(Category category);
    Optional<Category> findById(CategoryID id);
    Category update(Category category);
    void deleteById(CategoryID id);
    Pagination<Category> findAll(CategorySearchQuery query);
}

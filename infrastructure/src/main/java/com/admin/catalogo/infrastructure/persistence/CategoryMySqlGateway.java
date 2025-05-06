package com.admin.catalogo.infrastructure.persistence;


import com.admin.catalogo.domain.category.Category;
import com.admin.catalogo.domain.category.CategoryGateway;
import com.admin.catalogo.domain.category.CategoryID;
import com.admin.catalogo.domain.category.CategorySearchQuery;
import com.admin.catalogo.domain.pagination.Pagination;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryMySqlGateway implements CategoryGateway {

    private final CategoryRepository categoryRepository;

    public CategoryMySqlGateway(final CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category create(Category category) {
        var entity = CategoryJpaEntity.from(category);
        return this.categoryRepository.save(entity).toAggregate();

    }

    @Override
    public Optional<Category> findById(CategoryID id) {
        return this.categoryRepository.findById(id.getValue())
                .map(CategoryJpaEntity::toAggregate);
    }

    @Override
    public Category update(Category category) {
        var entity = CategoryJpaEntity.from(category);
        return this.categoryRepository.save(entity).toAggregate();
    }

    private Category save(final Category aCategory) {
        return this.categoryRepository.save(CategoryJpaEntity.from(aCategory)).toAggregate();
    }

    @Override
    public void deleteById(CategoryID id) {
        if(this.categoryRepository.existsById(id.getValue())){
            this.categoryRepository.deleteById(id.getValue());
        }
    }

    @Override
    public Pagination<Category> findAll(CategorySearchQuery query) {

        return null;
    }
}

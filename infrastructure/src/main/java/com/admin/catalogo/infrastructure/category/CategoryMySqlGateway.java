package com.admin.catalogo.infrastructure.category;


import com.admin.catalogo.domain.category.Category;
import com.admin.catalogo.domain.category.CategoryGateway;
import com.admin.catalogo.domain.category.CategoryID;
import com.admin.catalogo.domain.category.CategorySearchQuery;
import com.admin.catalogo.domain.pagination.Pagination;
import com.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import com.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import com.admin.catalogo.infrastructure.utils.SpecificationUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.admin.catalogo.infrastructure.utils.SpecificationUtils.like;

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

        final var page = PageRequest.of(query.page(), query.perPage(), Sort.by(Sort.Direction.fromString(query.direction()), query.sort()));
        this.categoryRepository.findAll();

        final var spectification = Optional.ofNullable(query.terms())
                .filter(str -> !str.isBlank())
                .map(str -> {
                    return SpecificationUtils.<CategoryJpaEntity>like("name", str).or(like("description", str));
                })
                .orElse(null);

        var data = this.categoryRepository.findAll(Specification.where(spectification), page);
        return new Pagination<>(data.getNumber(), data.getSize(), data.getTotalElements(), data.map(CategoryJpaEntity::toAggregate).toList());
    }
}

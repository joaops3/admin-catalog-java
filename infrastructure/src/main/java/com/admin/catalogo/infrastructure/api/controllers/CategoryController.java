package com.admin.catalogo.infrastructure.api.controllers;

import com.admin.catalogo.application.category.create.CreateCategoryCommand;
import com.admin.catalogo.application.category.create.CreateCategoryOutput;
import com.admin.catalogo.application.category.create.CreateCategoryUseCase;
import com.admin.catalogo.application.category.delete.DeleteCategoryUseCase;
import com.admin.catalogo.application.category.retrieve.get.CategoryOutput;
import com.admin.catalogo.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.admin.catalogo.application.category.retrieve.list.ListCategoriesUseCase;
import com.admin.catalogo.application.category.update.UpdateCategoryCommand;
import com.admin.catalogo.application.category.update.UpdateCategoryUseCase;
import com.admin.catalogo.domain.category.CategorySearchQuery;
import com.admin.catalogo.domain.pagination.Pagination;
import com.admin.catalogo.domain.validation.handler.Notification;
import com.admin.catalogo.infrastructure.api.CategoryAPI;
import com.admin.catalogo.infrastructure.category.models.CreateCategoryApiRequest;
import com.admin.catalogo.infrastructure.category.models.UpdateCategoryRequest;
import com.admin.catalogo.infrastructure.category.presenters.CategoryApiPresenter;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.Objects;
import java.util.function.Function;


public class CategoryController implements CategoryAPI {

    private final CreateCategoryUseCase createCategoryUseCase;

    private final GetCategoryByIdUseCase getCategoryByIdUseCase;

    private final UpdateCategoryUseCase updateCategoryUseCase;

    private final DeleteCategoryUseCase deleteCategoryUseCase;

    private final ListCategoriesUseCase listCategoriesUseCase;

    public CategoryController(CreateCategoryUseCase createCategoryUseCase, GetCategoryByIdUseCase getCategoryByIdUseCase, UpdateCategoryUseCase updateCategoryUseCase, DeleteCategoryUseCase deleteCategoryUseCase, ListCategoriesUseCase listCategoriesUseCase) {
        this.createCategoryUseCase = Objects.requireNonNull(createCategoryUseCase, "createCategoryUseCase must not be null");
        this.getCategoryByIdUseCase =  Objects.requireNonNull(getCategoryByIdUseCase, "getCategoryByIdUseCase must not be null");
        this.updateCategoryUseCase = updateCategoryUseCase;
        this.deleteCategoryUseCase = deleteCategoryUseCase;
        this.listCategoriesUseCase = listCategoriesUseCase;
    }

    @Override
    public ResponseEntity<?> createCategory(CreateCategoryApiRequest dto) {
        final var aCommand = CreateCategoryCommand.with(
                dto.name(),
                dto.description(),
                dto.active() != null ? dto.active() : true
        );

        final Function<Notification, ResponseEntity<?>> onError = notification -> {
            return ResponseEntity.unprocessableEntity().body(notification);
        };

        final Function<CreateCategoryOutput, ResponseEntity<?>> onSuccess = output -> {
            return ResponseEntity.created(URI.create("/Categories/"+ output.id())).body(output);
        };
        return this.createCategoryUseCase.execute(aCommand).fold(onError, onSuccess);
    }

    @Override
    public Pagination<?> ListCategories(String search, int page, int perPage, String sort, String direction) {
        var query = new CategorySearchQuery(page, perPage, search, sort, direction);
        var result = this.listCategoriesUseCase.execute(query);
        var response = result.map(CategoryApiPresenter::present);
        return null;
    }

    @Override
    public ResponseEntity<CategoryOutput> getCategoryById(String id) {
        var data = this.getCategoryByIdUseCase.execute(id);
        return ResponseEntity.ok(CategoryApiPresenter.present(data));
    }

    @Override
    public ResponseEntity<?> updateCategory(String id, UpdateCategoryRequest dto) {
        return updateCategoryUseCase.execute(UpdateCategoryCommand.with(
                id,
                dto.name(),
                dto.description(),
                dto.active() != null ? dto.active() : true
        )).fold(
                notification -> ResponseEntity.unprocessableEntity().body(notification),
                output -> ResponseEntity.ok().body(output)
        );
    }

    @Override
    public void deleteCategory(String id) {
        this.deleteCategoryUseCase.execute(id);
    }
}

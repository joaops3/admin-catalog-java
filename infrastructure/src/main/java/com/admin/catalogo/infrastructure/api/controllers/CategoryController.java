package com.admin.catalogo.infrastructure.api.controllers;

import com.admin.catalogo.application.category.create.CreateCategoryCommand;
import com.admin.catalogo.application.category.create.CreateCategoryOutput;
import com.admin.catalogo.application.category.create.CreateCategoryUseCase;
import com.admin.catalogo.application.category.retrieve.get.CategoryOutput;
import com.admin.catalogo.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.admin.catalogo.domain.pagination.Pagination;
import com.admin.catalogo.domain.validation.handler.Notification;
import com.admin.catalogo.infrastructure.api.CategoryAPI;
import com.admin.catalogo.infrastructure.category.models.CreateCategoryApiInput;
import com.admin.catalogo.infrastructure.category.presenters.CategoryApiPresenter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Objects;
import java.util.function.Function;


public class CategoryController implements CategoryAPI {

    private final CreateCategoryUseCase createCategoryUseCase;

    private final GetCategoryByIdUseCase getCategoryByIdUseCase;

    public CategoryController(CreateCategoryUseCase createCategoryUseCase, GetCategoryByIdUseCase getCategoryByIdUseCase) {
        this.createCategoryUseCase = Objects.requireNonNull(createCategoryUseCase, "createCategoryUseCase must not be null");
        this.getCategoryByIdUseCase =  Objects.requireNonNull(getCategoryByIdUseCase, "getCategoryByIdUseCase must not be null");
    }

    @Override
    public ResponseEntity<?> createCategory(CreateCategoryApiInput dto) {
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
        return null;
    }

    @Override
    public ResponseEntity<CategoryOutput> getCategoryById(String id) {
        var data = this.getCategoryByIdUseCase.execute(id);
        return ResponseEntity.ok(CategoryApiPresenter.present(data));
    }
}

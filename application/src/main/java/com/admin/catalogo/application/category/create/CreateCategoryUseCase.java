package com.admin.catalogo.application.category.create;

import com.admin.catalogo.application.UseCase;
import com.admin.catalogo.domain.category.CategoryGateway;
import com.admin.catalogo.domain.validation.handler.Notification;
import io.vavr.control.Either;

public class CreateCategoryUseCase extends UseCase<CreateCategoryCommand, Either<Notification, CreateCategoryOutput>> {

    protected final CategoryGateway categoryGateway;

    public CreateCategoryUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }


    @Override
    public Either<Notification, CreateCategoryOutput> execute(CreateCategoryCommand input) {
        return null;
    }
}

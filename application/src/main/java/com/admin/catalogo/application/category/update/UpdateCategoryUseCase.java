package com.admin.catalogo.application.category.update;

import com.admin.catalogo.application.UseCase;
import com.admin.catalogo.domain.category.CategoryGateway;
import com.admin.catalogo.domain.validation.handler.Notification;
import io.vavr.control.Either;

public class UpdateCategoryUseCase extends UseCase<UpdateCategoryCommand, Either<Notification, UpdateCategoryOutput>> {

    protected final CategoryGateway categoryGateway;

    public UpdateCategoryUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }

    @Override
    public Either<Notification, UpdateCategoryOutput> execute(UpdateCategoryCommand input) {
        return null;
    }
}

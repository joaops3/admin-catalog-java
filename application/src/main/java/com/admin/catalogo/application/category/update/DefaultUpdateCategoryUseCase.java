package com.admin.catalogo.application.category.update;

import com.admin.catalogo.domain.category.Category;
import com.admin.catalogo.domain.category.CategoryGateway;
import com.admin.catalogo.domain.category.CategoryID;
import com.admin.catalogo.domain.exceptions.DomainException;
import com.admin.catalogo.domain.validation.Error;
import com.admin.catalogo.domain.validation.handler.Notification;
import io.vavr.API;
import io.vavr.control.Either;

import java.util.function.Supplier;

public class DefaultUpdateCategoryUseCase extends  UpdateCategoryUseCase {

    public DefaultUpdateCategoryUseCase(final CategoryGateway categoryGateway) {
        super(categoryGateway);
    }

    @Override
    public Either<Notification, UpdateCategoryOutput> execute(final UpdateCategoryCommand aCommand) {
        final var aName = aCommand.name();
        final var aDescription = aCommand.description();
        final var isActive = aCommand.status();

        final var notification = Notification.create((Error) null);

        final var aCategory = this.categoryGateway.findById(CategoryID.from(aCommand.id()))
                .orElseThrow(notFound(aCommand.id()));
        aCategory.validate(notification);

        return notification.hasError() ? Either.left(notification) : update(aCategory);
    }

    private Supplier<DomainException> notFound(final String anId) {
        return () -> DomainException.with(new Error("Category with ID %s was not found".formatted(anId)));
    }

    private Either<Notification, UpdateCategoryOutput> update(final Category aCategory) {
        return API.Try(() -> this.categoryGateway.update(aCategory))
                .toEither()
                .map(UpdateCategoryOutput::from).mapLeft(Notification::create);
    }
}

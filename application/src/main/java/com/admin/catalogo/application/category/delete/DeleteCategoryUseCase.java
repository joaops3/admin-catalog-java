package com.admin.catalogo.application.category.delete;

import com.admin.catalogo.application.UnitUseCase;
import com.admin.catalogo.application.UseCase;
import com.admin.catalogo.domain.category.CategoryGateway;
import com.admin.catalogo.domain.validation.handler.Notification;
import io.vavr.control.Either;

public class DeleteCategoryUseCase extends UnitUseCase<String> {

    protected final CategoryGateway categoryGateway;

    public DeleteCategoryUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }






    @Override
    public void execute(String s) {

    }
}

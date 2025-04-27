package com.admin.catalogo.application.category.delete;

import com.admin.catalogo.domain.category.CategoryGateway;
import com.admin.catalogo.domain.category.CategoryID;
import com.admin.catalogo.domain.validation.Error;
import com.admin.catalogo.domain.validation.handler.Notification;
import io.vavr.control.Either;

public class DefaultDeleteCategoryUseCase extends DeleteCategoryUseCase {

    public DefaultDeleteCategoryUseCase(final CategoryGateway categoryGateway) {
        super(categoryGateway);
    }

    @Override
    public void execute(String id) {

        this.categoryGateway.deleteById(CategoryID.from(id));



    }
}

package com.admin.catalogo.domain.category;

import com.admin.catalogo.domain.validation.Error;
import com.admin.catalogo.domain.validation.ValidationHandler;
import com.admin.catalogo.domain.validation.Validator;

import java.util.Objects;

public class CategoryValidator extends Validator {

    private final Category category;

    protected CategoryValidator(final Category aCategory,  ValidationHandler aHandler) {
        super(aHandler);
        this.category = aCategory;
    }

    @Override
    public void Validate() {
        if(this.category.getName() == null){
            this.validationHandler().append(new Error("'name' should be not null"));
        }

        if(Objects.equals(this.category.getName(), "")){
            this.validationHandler().append(new Error("'name' should be not empty"));
        }

        if(this.category.getName().length() > 50){
            this.validationHandler().append(new Error("'name' should be be less than 50"));
        }

        if(this.category.getName().length() < 3){
            this.validationHandler().append(new Error("'name' should be not be less than 3"));
        }

    }
}

package com.admin.catalogo.domain.validation;

public abstract class Validator {

    final ValidationHandler handler;

    protected Validator(final ValidationHandler aHandler){
            this.handler = aHandler;
    }

    public abstract void Validate();

    protected  ValidationHandler validationHandler(){
        return this.handler;
    }
}

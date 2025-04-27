package com.admin.catalogo.domain.validation.handler;

import com.admin.catalogo.domain.exceptions.DomainException;
import com.admin.catalogo.domain.validation.Error;
import com.admin.catalogo.domain.validation.ValidationHandler;

import java.util.ArrayList;
import java.util.List;

public class Notification implements ValidationHandler {

    private final List<Error> errors;

    private Notification(final List<Error> errors) {
        this.errors = errors;
    }

    public static Notification create(final Error anError) {
        return (Notification) new Notification(new ArrayList<>()).append(anError);
    }

    public static Notification create(final Throwable t) {
        return create(new Error(t.getMessage()));
    }

    @Override
    public List<Error> getErrors() {
        return this.errors;
    }

    @Override
    public ValidationHandler append(final Error anError) {
        this.errors.add(anError);
        return null;
    }

    @Override
    public ValidationHandler append(ValidationHandler anHandler) {
        this.errors.addAll(anHandler.getErrors());
        return null;
    }

    @Override
    public ValidationHandler validate(Validation aValidation) {
        try{
            aValidation.validate();

        }catch (final DomainException e){
            this.errors.addAll(e.getErrors());
        }catch (final Throwable t){
            this.errors.add(new Error(t.getMessage()));
        }
        return this;
    }
}

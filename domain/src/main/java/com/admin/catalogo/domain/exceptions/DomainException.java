package com.admin.catalogo.domain.exceptions;

import com.admin.catalogo.domain.validation.Error;

import java.util.List;

public class DomainException extends NoStackTraceException {
    private final List<Error> errors;

    public DomainException(final String message,  final List<Error> anErrors){
        super(message);
        this.errors = anErrors;
    }

    public static DomainException with(final Error anErrors){
        return new DomainException(anErrors.message(), List.of(anErrors));
    }

    public static DomainException with(final List<Error> anErrors) {
        return new DomainException("Multiple errors occurred", anErrors);
    }

    public List<Error> getErrors() {
        return errors;
    }
}

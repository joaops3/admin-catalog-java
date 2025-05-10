package com.admin.catalogo.infrastructure.api.controllers;


import com.admin.catalogo.domain.exceptions.DomainException;
import com.admin.catalogo.domain.exceptions.NotFoundException;
import com.admin.catalogo.domain.validation.Error;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.NotActiveException;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<?> handleNotFoundException(final NotFoundException ex,
                                                   final HttpServletRequest request,
                                                   final HttpServletResponse response) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiError.from(ex));
    }

    @ExceptionHandler(value = {DomainException.class})
    public ResponseEntity<?> handleDomainException(final DomainException ex,
                                                   final HttpServletRequest request,
                                                   final HttpServletResponse response) {
        return ResponseEntity.unprocessableEntity().body(ApiError.from(ex));
    }

    record ApiError(String message, List<Error> errors){
        static ApiError from(final DomainException ex){
            return new ApiError(ex.getMessage(), ex.getErrors());
        }
    }
}

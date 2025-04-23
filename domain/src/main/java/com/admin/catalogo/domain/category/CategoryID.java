package com.admin.catalogo.domain.category;

import com.admin.catalogo.domain.Identifier;

import java.util.Objects;
import java.util.UUID;

public class CategoryID extends Identifier {
    private final String value;

    private CategoryID(String value) {
        Objects.requireNonNull(value);
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static CategoryID unique(){
        return new CategoryID(UUID.randomUUID().toString().toLowerCase());
    }

    public static CategoryID from(final String anId){
        return new CategoryID(anId);
    }

    public static CategoryID from(final UUID anId){
        return new CategoryID(anId.toString().toLowerCase());
    }
}

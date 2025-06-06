package com.admin.catalogo.infrastructure.category.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateCategoryApiRequest(@JsonProperty("name") String name,
                                       @JsonProperty("description") String description,
                                       @JsonProperty("is_active") Boolean active) {
}

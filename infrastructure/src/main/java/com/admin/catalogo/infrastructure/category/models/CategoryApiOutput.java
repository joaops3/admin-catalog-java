package com.admin.catalogo.infrastructure.category.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record CategoryApiOutput(
        @JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("description") String description,
        @JsonProperty("is_active") Boolean active,
        @JsonProperty("created_at") Instant created_at,
        @JsonProperty("updated_at") Instant updated_at,
        @JsonProperty("deleted_at") Instant deleted_at) {
}

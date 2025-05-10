package com.admin.catalogo.infrastructure.api;

import com.admin.catalogo.application.category.retrieve.get.CategoryOutput;
import com.admin.catalogo.domain.pagination.Pagination;
import com.admin.catalogo.infrastructure.category.models.CreateCategoryApiInput;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/categories")
@Tag(name = "Categories")
public interface CategoryAPI {


    @PostMapping(consumes= MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new category")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category created successfully"),
            @ApiResponse(responseCode = "422", description = "Unprocessable error"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    public ResponseEntity<?> createCategory(@RequestBody @Valid CreateCategoryApiInput dto);

    @GetMapping
    @Operation(summary = "List all categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categories listed successfully"),
            @ApiResponse(responseCode = "422", description = "Unprocessable error"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    public Pagination<?> ListCategories(@RequestParam(name="search", required = false, defaultValue = "") final String search,
                                        @RequestParam(name="page", required = false, defaultValue = "0") final int page,
                                        @RequestParam(name="perPage", required = false, defaultValue = "10") final int perPage,
                                        @RequestParam(name="sort", required = false, defaultValue = "name") final String sort,
                                        @RequestParam(name="dir", required = false, defaultValue = "asc") final String direction
    );

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get a category by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category found successfully"),
            @ApiResponse(responseCode = "422", description = "Unprocessable error"),
            @ApiResponse(responseCode = "500", description = "An internal server error was thrown")
    })
    public ResponseEntity<CategoryOutput> getCategoryById(@PathVariable(name = "id") final String id);
}

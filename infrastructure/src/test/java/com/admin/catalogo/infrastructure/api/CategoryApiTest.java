package com.admin.catalogo.infrastructure.api;


import com.admin.catalogo.ControllerTest;
import com.admin.catalogo.application.category.create.CreateCategoryOutput;
import com.admin.catalogo.application.category.create.CreateCategoryUseCase;
import com.admin.catalogo.application.category.delete.DeleteCategoryUseCase;
import com.admin.catalogo.application.category.retrieve.get.CategoryOutput;
import com.admin.catalogo.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.admin.catalogo.application.category.retrieve.list.CategoryListOutput;
import com.admin.catalogo.application.category.retrieve.list.ListCategoriesUseCase;
import com.admin.catalogo.application.category.update.UpdateCategoryOutput;
import com.admin.catalogo.application.category.update.UpdateCategoryUseCase;
import com.admin.catalogo.domain.category.Category;
import com.admin.catalogo.domain.category.CategoryID;
import com.admin.catalogo.domain.exceptions.NotFoundException;
import com.admin.catalogo.domain.pagination.Pagination;
import com.admin.catalogo.domain.validation.Error;
import com.admin.catalogo.domain.validation.handler.Notification;
import com.admin.catalogo.infrastructure.category.models.CreateCategoryApiRequest;
import com.admin.catalogo.infrastructure.category.models.UpdateCategoryRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Either;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Objects;

import static io.vavr.control.Option.when;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = CategoryAPI.class)
public class CategoryApiTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CreateCategoryUseCase createCategoryUseCase;

    @MockBean
    private GetCategoryByIdUseCase getCategoryByIdUseCase;


    @MockBean
    private UpdateCategoryUseCase updateCategoryUseCase;

    @MockBean
    private DeleteCategoryUseCase deleteCategoryUseCase;

    @MockBean
    private ListCategoriesUseCase listCategoriesUseCase;

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void givenAValidCommand_whenCallsCreateCategory_shouldReturnCategoryId() throws Exception {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;


        Mockito.when(createCategoryUseCase.execute(Mockito.any()))
                .thenReturn(Either.right(CreateCategoryOutput.from(CategoryID.from("123"))));

        final var aRequest = new CreateCategoryApiRequest(expectedName, expectedDescription, expectedIsActive);
        final var request = MockMvcRequestBuilders
                .post("/categories")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(this.mapper.writeValueAsString(aRequest))
                .accept("application/json");
        this.mvc.perform(request)
                .andDo(print())
                .andExpectAll(status().isCreated(),
                        header().string("Location", "/categories/"),
                        header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE));
        verify(createCategoryUseCase, times(1)).execute(argThat(aCommand ->
                aCommand.name().equals(expectedName)
                        && aCommand.description().equals(expectedDescription)
                        && aCommand.isActive() == expectedIsActive
        ));
    }

    @Test
    public void givenAInvalidCommand_whenCallsCreateCategory_shouldReturnError() throws Exception {
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";

        Mockito.when(createCategoryUseCase.execute(Mockito.any()))
                .thenReturn(Either.left(Notification.create(new Error(expectedErrorMessage))));

        final var aRequest = new CreateCategoryApiRequest(expectedName, expectedDescription, expectedIsActive);
        final var request = MockMvcRequestBuilders
                .post("/categories")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(this.mapper.writeValueAsString(aRequest))
                .accept("application/json");
        this.mvc.perform(request)
                .andDo(print())
                .andExpectAll(status().isUnprocessableEntity(),
                        header().string("Location", "/categories/"),
                        header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE),
                        jsonPath("$.message", equalTo(expectedErrorMessage)),
                        jsonPath("$.size", hasSize(1)),
                        jsonPath("$.errors[0].message", equalTo(expectedErrorMessage))
                );


        verify(createCategoryUseCase, times(1)).execute(argThat(aCommand ->
                aCommand.name().equals(expectedName)
                        && aCommand.description().equals(expectedDescription)
                        && aCommand.isActive() == expectedIsActive
        ));
    }


    @Test
    public void givenAValidCommand_whenCallsGetCategorybyId_shouldReturnCategory() throws Exception {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var expectedCategory = CategoryOutput.from(Category.newCategory(
                expectedName,
                expectedDescription,
                expectedIsActive
        ));

        Mockito.when(getCategoryByIdUseCase.execute(Mockito.any()))
                .thenReturn(expectedCategory);

        final var request = MockMvcRequestBuilders
                .get("/categories/${id}", expectedCategory.id().getValue())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept("application/json");

       final var response = this.mvc.perform(request)
                .andDo(print());

        response.andExpect(status().isOk())
                .andExpectAll(
                        header().string("Location", "/categories/"),
                        header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE),
                        jsonPath("$.id", equalTo(expectedCategory.id().getValue())),
                        jsonPath("$.name", equalTo(expectedName)),
                        jsonPath("$.description", equalTo(expectedDescription)),
                        jsonPath("$.isActive", equalTo(expectedIsActive))
                );


        verify(getCategoryByIdUseCase, times(1)).execute(eq(expectedCategory.id().getValue()));
    }

    @Test
    public void givenAInvalidCommand_whenCallsGetCategorybyId_shouldReturnNotFound() throws Exception {

        final var expectedErrorMessage = "Category with ID 123 not found";
        final var expectedId = CategoryID.from("123");
        Mockito.when(getCategoryByIdUseCase.execute(Mockito.any()))
                .thenThrow(NotFoundException.with(Category.class,
                        expectedId
                ));

        final var request = MockMvcRequestBuilders
                .get("/categories/${id}", expectedId.getValue())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept("application/json");

        final var response = this.mvc.perform(request)
                .andDo(print());

        response.andExpect(status().isNotFound())
                .andExpectAll(
                        header().string("Location", "/categories/"),
                        header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE),
                        jsonPath("$.message", equalTo("Category with ID 1234567890 not found"))
                );

       verify(getCategoryByIdUseCase, times(1)).execute(eq(expectedId.getValue()));
    }

    @Test
    public void givenAValidCommand_whenCallsUpdateCategory_shouldReturnUpdated() throws Exception {

        final var expectedId = "123";
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        Mockito.when(updateCategoryUseCase.execute(Mockito.any()))
                .thenReturn(Either.right(UpdateCategoryOutput.from(CategoryID.from(expectedId).getValue())));
        final var input = new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);
        final var request = MockMvcRequestBuilders
                .put("/categories/${id}", expectedId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(this.mapper.writeValueAsString(input))
                .accept("application/json");

        final var response = this.mvc.perform(request)
                .andDo(print());

        response.andExpect(status().isNotFound())
                .andExpectAll(
                        header().string("Location", "/categories/"),
                        header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE)

                );

        verify(updateCategoryUseCase, times(1)).execute(argThat(cmd -> Objects.equals(cmd.name(), expectedName)
                && Objects.equals(cmd.description(), expectedDescription)

                && cmd.id().equals(expectedId)
        ));
    }

    @Test
    public void givenAValidCommand_whenCallsDeleteById_shouldReturnNotFound() throws Exception {


        final var expectedId = CategoryID.from("123");
        Mockito.doNothing().when(getCategoryByIdUseCase.execute(Mockito.any()));


        final var request = MockMvcRequestBuilders
                .delete("/categories/${id}", expectedId.getValue())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept("application/json");

        final var response = this.mvc.perform(request)
                .andDo(print());

        response.andExpect(status().isNoContent())
                .andExpectAll(
                        header().string("Location", "/categories/"),
                        header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE)

                );

        verify(deleteCategoryUseCase, times(1)).execute(eq(expectedId.getValue()));
    }


    @Test
    public void givenAValidparams_whenCallsListCategories_shouldReturnList() throws Exception {
        final var aCategory = Category.newCategory("Movies", null, true);

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "movies";
        final var expectedSort = "description";
        final var expectedDirection = "desc";
        final var expectedItemsCount = 1;
        final var expectedTotal = 1;

        final var expectedItems = List.of(CategoryListOutput.from(aCategory));

        Mockito.when(listCategoriesUseCase.execute(Mockito.any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        final var request = MockMvcRequestBuilders.get("/categories")
                .queryParam("page", String.valueOf(expectedPage))
                .queryParam("perPage", String.valueOf(expectedPerPage))
                .queryParam("sort", expectedSort)
                .queryParam("dir", expectedDirection)
                .queryParam("search", expectedTerms)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request)
                .andDo(print());


        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(expectedPage)))
                .andExpect(jsonPath("$.per_page", equalTo(expectedPerPage)))
                .andExpect(jsonPath("$.total", equalTo(expectedTotal)))
                .andExpect(jsonPath("$.items", hasSize(expectedItemsCount)))
                .andExpect(jsonPath("$.items[0].id", equalTo(aCategory.getId().getValue())))
                .andExpect(jsonPath("$.items[0].name", equalTo(aCategory.getName())))
                .andExpect(jsonPath("$.items[0].description", equalTo(aCategory.getDescription())))
                .andExpect(jsonPath("$.items[0].is_active", equalTo(aCategory.isActive())))
                .andExpect(jsonPath("$.items[0].created_at", equalTo(aCategory.getCreatedAt().toString())))
                .andExpect(jsonPath("$.items[0].deleted_at", equalTo(aCategory.getDeletedAt())));


        verify(listCategoriesUseCase, times(1)).execute(argThat(query ->
                Objects.equals(expectedPage, query.page())
                        && Objects.equals(expectedPerPage, query.perPage())
                        && Objects.equals(expectedDirection, query.direction())
                        && Objects.equals(expectedSort, query.sort())
                        && Objects.equals(expectedTerms, query.terms())
        ));
    }
}

package com.admin.catalogo.infrastructure.api;


import com.admin.catalogo.ControllerTest;
import com.admin.catalogo.application.category.create.CreateCategoryOutput;
import com.admin.catalogo.application.category.create.CreateCategoryUseCase;
import com.admin.catalogo.application.category.retrieve.get.CategoryOutput;
import com.admin.catalogo.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.admin.catalogo.domain.category.Category;
import com.admin.catalogo.domain.category.CategoryID;
import com.admin.catalogo.domain.exceptions.DomainException;
import com.admin.catalogo.domain.exceptions.NotFoundException;
import com.admin.catalogo.domain.validation.Error;
import com.admin.catalogo.domain.validation.handler.Notification;
import com.admin.catalogo.infrastructure.category.models.CreateCategoryApiInput;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Either;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

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

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void givenAValidCommand_whenCallsCreateCategory_shouldReturnCategoryId() throws Exception {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;


        Mockito.when(createCategoryUseCase.execute(Mockito.any()))
                .thenReturn(Either.right(CreateCategoryOutput.from(CategoryID.from("123"))));

        final var aRequest = new CreateCategoryApiInput(expectedName, expectedDescription, expectedIsActive);
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

        final var aRequest = new CreateCategoryApiInput(expectedName, expectedDescription, expectedIsActive);
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
}

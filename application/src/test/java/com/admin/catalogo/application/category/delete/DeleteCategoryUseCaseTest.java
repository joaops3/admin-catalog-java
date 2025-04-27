package com.admin.catalogo.application.category.delete;

import com.admin.catalogo.domain.category.Category;
import com.admin.catalogo.domain.category.CategoryGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static javax.management.Query.eq;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
public class DeleteCategoryUseCaseTest {

    @InjectMocks
    private DefaultDeleteCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(categoryGateway);
    }

    @Test
    public void givenAValidCommand_whenCallsDeleteCategory_shouldBeOK() {
        // Given
        final var category = Category.newCategory("filmes", "filmes de terror", true);


        // When
        Mockito.doNothing().when(categoryGateway).deleteById(category.getId());
        useCase.execute(category.getId().getValue());



        // Then
        Mockito.verify(categoryGateway, Mockito.times(1)).deleteById(category.getId());
    }

    @Test
    public void givenAInvalidCommand_whenCallsDeleteCategory_ShouldReturnException(){
        // Given
        final var category = Category.newCategory("filmes", "filmes de terror", true);


        // When
        doThrow(new IllegalStateException("Category not found"))
                .when(categoryGateway).deleteById(category.getId());

        // Then
        Assertions.assertThrows(IllegalStateException.class, () -> {
            useCase.execute(category.getId().getValue());
        });

        Mockito.verify(categoryGateway, Mockito.times(1)).deleteById(category.getId());
    }

}

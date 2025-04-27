package com.admin.catalogo.application.category.update;


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

import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.argThat;


@ExtendWith(MockitoExtension.class)
public class UpdateCategoryUseCaseTest {

    @InjectMocks
    private DefaultUpdateCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(categoryGateway);
    }

    @Test
    public void givenAInvalidCommand_whenCallsUpdateCategory_shouldReturnValidationError() {
        final var expectedName = "filmes";
        final var expectedDescription = "filmes de terror";
        final var expectedIsActive = true;
        final var expectedCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var categoryCommand = UpdateCategoryCommand.with(expectedCategory.getId().getValue(), expectedName, expectedName, expectedIsActive);

        Mockito.when(categoryGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(expectedCategory.clone()));
        Mockito.when(categoryGateway.update(Mockito.any()))
                .thenReturn((Category) returnsFirstArg());

        var output = useCase.execute(categoryCommand);

        Assertions.assertNotNull(output);

        Mockito.verify(categoryGateway, Mockito.times(1)).findById(Mockito.any());
        Mockito.verify(categoryGateway, Mockito.times(1)).update(argThat(category -> {
            Assertions.assertEquals(expectedName, category.getName());
            Assertions.assertEquals(expectedDescription, category.getDescription());
            Assertions.assertEquals(expectedIsActive, category.isActive());
            return true;
        }));
    }
}

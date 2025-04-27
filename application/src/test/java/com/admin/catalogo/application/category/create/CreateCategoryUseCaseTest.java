package com.admin.catalogo.application.category.create;

import com.admin.catalogo.domain.category.Category;
import com.admin.catalogo.domain.category.CategoryGateway;
import com.admin.catalogo.domain.exceptions.DomainException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CreateCategoryUseCaseTest {

    @InjectMocks
    private DefaultCreateCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(categoryGateway);
    }

    @Test
    public void givenAValidCommand_whenCallsCreateCategory_shouldReturnCategoryId(){
        final var expectedName = "filmes";
        final var expectedDescription = "filmes de terror";
        final var expectedIsActive = true;
        final var expectedCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        final var categoryCommand = CreateCategoryCommand.with(expectedName, expectedName, expectedIsActive);

        final CategoryGateway categoryGateway = Mockito.mock(CategoryGateway.class);
        when(categoryGateway.create(any()))
                .thenAnswer(returnFirstArg -> returnFirstArg.getArgument(0));

        final var useCase = new CreateCategoryUseCase(categoryGateway);
        final var actualOutput = useCase.execute(categoryCommand);

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.get().id());
        Mockito.verify(categoryGateway, times(1))
                .create(Mockito.argThat(aCategory -> Objects.equals(expectedName, aCategory.getName()) &&
                        Objects.equals(expectedDescription, aCategory.getDescription()) &&
                        Objects.equals(expectedIsActive, aCategory.isActive()) && Objects.nonNull(aCategory.getId())));

    }

    @Test
    public void givenAInvalidName_whenCallsCreateCategory_shouldReturnDomainException(){
        final String expectedName = null;
        final String expectedDescription = "filmes de terror";
        final boolean expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var categoryCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        when(categoryGateway.create(any())).thenAnswer(returnsFirstArg());

        var notification = useCase.execute(categoryCommand);

        Assertions.assertThrows(DomainException.class, notification::getLeft);
        Assertions.assertEquals(expectedErrorCount, notification.getLeft().getErrors().size());
        Mockito.verify(categoryGateway, times(0)).create(any());
    }
}

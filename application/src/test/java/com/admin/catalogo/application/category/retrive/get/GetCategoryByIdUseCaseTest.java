package com.admin.catalogo.application.category.retrive.get;

import com.admin.catalogo.application.category.retrieve.get.DefaultCategoryByIdUseCase;
import com.admin.catalogo.domain.category.Category;
import com.admin.catalogo.domain.category.CategoryGateway;
import com.admin.catalogo.domain.category.CategoryID;
import com.admin.catalogo.domain.exceptions.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;


@ExtendWith(MockitoExtension.class)
public class GetCategoryByIdUseCaseTest {

    @InjectMocks
    private DefaultCategoryByIdUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(categoryGateway);
    }

    @Test
    public void givenAValidCommand_whenCallGetCategoryById_shouldReturn() {
            final var category = Category.newCategory("filmes", "filmes de terror", true);


            Mockito.when(this.categoryGateway.findById(Mockito.any())).thenReturn(Optional.of(category.clone()));

            var found = useCase.execute(category.getId().getValue());

            Assertions.assertEquals(category.getId(), found.id());
            Mockito.verify(categoryGateway, Mockito.times(1)).findById(Mockito.any());
    }

    @Test
    public void givenAValidCommand_whenCallGetCategoryById_shouldNotReturn() {
        final var expectedErrorMessage = "Category with ID 123 was not found";
        final var expectedId = CategoryID.from("123");



        Mockito.when(categoryGateway.findById(expectedId))
                .thenReturn(Optional.empty());

        final var actualException = Assertions.assertThrows(
                NotFoundException.class,
                () -> useCase.execute(expectedId.getValue())
        );

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }
}

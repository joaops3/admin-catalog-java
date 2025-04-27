package com.admin.catalogo.application.category.retrive.list;



import com.admin.catalogo.application.category.retrieve.list.CategoryListOutput;
import com.admin.catalogo.application.category.retrieve.list.DefaultCategoryListUseCase;
import com.admin.catalogo.domain.category.Category;
import com.admin.catalogo.domain.category.CategoryGateway;
import com.admin.catalogo.domain.category.CategorySearchQuery;
import com.admin.catalogo.domain.pagination.Pagination;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;

@ExtendWith(MockitoExtension.class)
public class ListCategoriesUseCaseTest {

    @InjectMocks
    private DefaultCategoryListUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(categoryGateway);
    }


    @Test
    public void givenAValidQuery_whenCallList_shouldReturn(){
        final var expectedItemsCount = 2;


        final var query = new CategorySearchQuery(
                0,
                10,
                "",
                "createdAt",
                "asc"
        );

        var categories = List.of(
                Category.newCategory("filmes", "filmes de terror", true),
                Category.newCategory("filmes2", "2", true)
        );

        var pagination = new Pagination<Category>(1, 10, categories.size(), categories);
        final var expectedResult = pagination.map(CategoryListOutput::from);
        Mockito.when(categoryGateway.findAll(Mockito.any()))
                .thenReturn(pagination);
        final var result = useCase.execute(query);

        Assertions.assertEquals(expectedItemsCount, result.items().size());
        Assertions.assertEquals(expectedResult.items().size(), result.items().size());
    }

    @Test
    public void givenAInvalidCommand_whenHasNoResult_shouldReturnEmptyCategories(){
        final var expectedItemsCount = 0;


        final var query = new CategorySearchQuery(
                0,
                10,
                "",
                "createdAt",
                "asc"
        );

        var categories = List.<Category>of(

        );

        var pagination = new Pagination<Category>(1, 10, categories.size(), categories);
        final var expectedResult = pagination.map(CategoryListOutput::from);
        Mockito.when(categoryGateway.findAll(Mockito.any()))
                .thenReturn(pagination);
        final var result = useCase.execute(query);

        Assertions.assertEquals(expectedItemsCount, result.items().size());
        Assertions.assertEquals(expectedResult.items().size(), result.items().size());
    }

    @Test
    public void givenAValidCommand_whenGatewayThrowsException_shouldReturnException(){
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedErrorMessage = "Gateway error";

        final var aQuery =
                new CategorySearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        Mockito.when(categoryGateway.findAll(eq(aQuery)))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        final var actualException =
                Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(aQuery));

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }
}

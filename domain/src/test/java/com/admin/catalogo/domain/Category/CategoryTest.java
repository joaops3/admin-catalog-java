package com.admin.catalogo.domain.Category;

import com.admin.catalogo.domain.category.Category;
import com.admin.catalogo.domain.exceptions.DomainException;
import com.admin.catalogo.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CategoryTest {


    @Test
    public void givenAValidParams_whenCallNewCategory_thenInstantiateCategory() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var category = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        Assertions.assertNotNull(category);
        Assertions.assertNotNull(category.getId());
        Assertions.assertEquals(expectedName, category.getName());
    }

    @Test
    public void givenAnInvalidNullName_whenCallNewCategory_thenShouldReceiveError() {
        final String expectedName = null;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should be not null";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var category = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var actualException = Assertions.assertThrows(DomainException.class, () -> category.validate(new ThrowsValidationHandler()) );

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidEmptyName_whenCallNewCategory_thenShouldReceiveError() {
        final String expectedName = "a";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should be not empty";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var category = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var actualException = Assertions.assertThrows(DomainException.class, () -> category.validate(new ThrowsValidationHandler()) );

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidNameLessThan3_whenCallNewCategory_thenShouldReceiveError() {
        final String expectedName = "a";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should be not be less than 3";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var category = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var actualException = Assertions.assertThrows(DomainException.class, () -> category.validate(new ThrowsValidationHandler()) );

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidNameBiggerThan50_whenCallNewCategory_thenShouldReceiveError() {
        final String expectedName = """
                Sed ut perspiciatis unde omnis iste natus error sit voluptatem
                accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt. Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem. Ut enim ad minima veniam, quis
                """;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should be be less than 50";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var category = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var actualException = Assertions.assertThrows(DomainException.class, () -> category.validate(new ThrowsValidationHandler()) );

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }


    @Test
    public void givenAnValidActiveCategory_whenCallDeactive_thenShouldReturnDeactiveCategory() {
        final String expectedName = "a";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var category = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        var updatedAt = category.getUpdatedAt();
        Assertions.assertTrue(category.isActive());

        final var actualCategory = category.deactivate();
        var newUpdatedAt = category.getUpdatedAt();
        Assertions.assertTrue(newUpdatedAt.isAfter(updatedAt));
        Assertions.assertNotNull(actualCategory);
        Assertions.assertFalse(actualCategory.isActive());
    }

    @Test
    public void givenAnValidInactiveCategory_whenCallactive_thenShouldReturnactiveCategory() {
        final String expectedName = "a";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;

        final var category = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        var updatedAt = category.getUpdatedAt();
        Assertions.assertFalse(category.isActive());

        final var actualCategory = category.activate();
        var newUpdatedAt = category.getUpdatedAt();
        Assertions.assertTrue(newUpdatedAt.isAfter(updatedAt));
        Assertions.assertNotNull(actualCategory);
        Assertions.assertTrue(actualCategory.isActive());
    }


    @Test
    public void givenAValidCategory_whenCallUpdate_thenShouldReturnUpdatedCategory() {
        final String expectedName = "a";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var category = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        var updatedAt = category.getUpdatedAt();
        Assertions.assertTrue(category.isActive());

        final var actualCategory = category.update("b", "A categoria", false);

        var newUpdatedAt = category.getUpdatedAt();
        Assertions.assertTrue(newUpdatedAt.isAfter(updatedAt));
        Assertions.assertNotNull(actualCategory);
        Assertions.assertFalse(actualCategory.isActive());
    }
}

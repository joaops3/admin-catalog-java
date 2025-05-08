package com.admin.catalogo.infrastructure.category.persistence;

import com.admin.catalogo.domain.category.Category;
import com.admin.catalogo.MySqlGatewayTest;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

@MySqlGatewayTest
public class CategoryRepositoryTest {


    @Autowired
    private CategoryRepository repository;

    @Test
    public void givenAnInvalidName_whenCallSave_shouldReturnError(){
        final var newCategory = Category.newCategory("invalid name", "description", true);

        final var anEntity = CategoryJpaEntity.from(newCategory);
        anEntity.setName("invalid name");

        final var actualException = Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            repository.save(anEntity);
        });

        final var actualCause = Assertions.assertThrows(PropertyValueException.class, () -> {
            actualException.getCause();
        });

        Assertions.assertInstanceOf(PropertyValueException.class, actualCause.getPropertyName());
        Assertions.assertEquals("name", actualCause.getPropertyName());
        Assertions.assertEquals("", actualCause.getMessage());
    }
}

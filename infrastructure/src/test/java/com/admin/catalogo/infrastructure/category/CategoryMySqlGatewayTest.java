package com.admin.catalogo.infrastructure.category;


import com.admin.catalogo.domain.category.Category;
import com.admin.catalogo.domain.category.CategoryID;
import com.admin.catalogo.domain.category.CategorySearchQuery;
import com.admin.catalogo.MySqlGatewayTest;
import com.admin.catalogo.category.persistence.CategoryJpaEntity;
import com.admin.catalogo.category.persistence.CategoryMySqlGateway;
import com.admin.catalogo.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@MySqlGatewayTest
public class CategoryMySqlGatewayTest {

    @Autowired
    private CategoryMySqlGateway categoryGateway;

    @Autowired
    private CategoryRepository categoryRepository;

    void cleanup() {
        // Cleanup logic if needed
    }

    @Test
    public void testInjectDependency() {
        Assertions.assertNotNull(categoryGateway);
        Assertions.assertNotNull(categoryRepository);
    }


    @Test
    public void givenAValidCategory_whenCreate_thenShouldReturnNewCategory() {
        // Given
        var expectedName = "Category Name";
        var expectedDescription = "Category Description";
        var expectedIsActive = true;
        final var category = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        // When
        // Call the create method
        Assertions.assertEquals(0, categoryRepository.count());

        final var actualCategory = categoryGateway.create(category);



        // Then
        // Assert that the category is persisted correctly
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(category.getCreatedAt(), actualCategory.getCreatedAt());
        Assertions.assertEquals(category.getUpdatedAt(), actualCategory.getUpdatedAt());
        Assertions.assertEquals(category.getDeletedAt(), actualCategory.getDeletedAt());
    }


    @Test
    public void givenAValidCategory_whenUpdate_thenShouldReturnUpdated() {
        // Given
        var expectedName = "Category Name";
        var expectedDescription = "Category Description";
        var expectedIsActive = true;
        final var category = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        // When
        // Call the create method
        categoryRepository.saveAndFlush(CategoryJpaEntity.from(category)).toAggregate();
        Assertions.assertEquals(1, categoryRepository.count());

        final var aActualCategory = category.clone().update("Updated Name", "Updated Description", false);

        final var actualCategory = categoryGateway.update(aActualCategory);

        // Then
        // Assert that the category is persisted correctly
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(aActualCategory.getCreatedAt(), actualCategory.getCreatedAt());
        Assertions.assertEquals(aActualCategory.getUpdatedAt(), actualCategory.getUpdatedAt());
        Assertions.assertEquals(aActualCategory.getDeletedAt(), actualCategory.getDeletedAt());
    }


    @Test
    public void givenAValidCategoryAndAValidId_whenTryToDeletedId_shouldDeleteCategory() {
        // Given
        var expectedName = "Category Name";
        var expectedDescription = "Category Description";
        var expectedIsActive = true;
        final var category = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        // When
        // Call the create method
        categoryRepository.saveAndFlush(CategoryJpaEntity.from(category)).toAggregate();
        Assertions.assertEquals(1, categoryRepository.count());

        final var aActualCategory = category.clone().update("Updated Name", "Updated Description", false);


        categoryGateway.deleteById(aActualCategory.getId());

        // Then
        Assertions.assertEquals(1, categoryRepository.count());

    }


    @Test
    public void givenAnInvalidCategoryAndAValidId_whenTryToDeletedId_shouldDeleteCategory() {

        Assertions.assertEquals(0, categoryRepository.count());

        categoryGateway.deleteById(CategoryID.from("1234567890"));

        // Then
        Assertions.assertEquals(0, categoryRepository.count());

    }

    @Test
    public void givenAnValidCategoryAndValidId_whenCallsFindById_shouldReturnCategory() {

        // Given
        var expectedName = "Category Name";
        var expectedDescription = "Category Description";
        var expectedIsActive = true;
        final var category = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        // When
        // Call the create method
        categoryRepository.saveAndFlush(CategoryJpaEntity.from(category)).toAggregate();
        Assertions.assertEquals(1, categoryRepository.count());

        final var aActualCategory = categoryGateway.findById(category.getId()).get();

        // Then
        Assertions.assertNotNull(aActualCategory);

    }

    @Test
    public void givenPrePersistedCategories_whenCallsFindAll_shouldReturnPaginated(){
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final var filmes1 = Category.newCategory("Filmes", "Filmes", true);
        final var filmes2 = Category.newCategory("Filmes2", "Filmes2", true);
        final var filmes3 = Category.newCategory("Filmes3", "Filmes3", true);

        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAll(List.of(CategoryJpaEntity.from(filmes1), CategoryJpaEntity.from(filmes2), CategoryJpaEntity.from(filmes3)));

        Assertions.assertEquals(3, categoryRepository.count());

        final var query = new CategorySearchQuery(0, 1, "", "name", "asc");
        final var actualResult = categoryGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(filmes1.getId(), actualResult.items().get(0).getId());
    }

    @Test
    public void givenEmptyCategoriesTable_whenCallsFindAll_shouldReturnEmptyPage(){
        Assertions.assertEquals(0, categoryRepository.count());

        final var query = new CategorySearchQuery(0, 1, "", "name", "asc");
        final var actualResult = categoryGateway.findAll(query);

        Assertions.assertEquals(0, actualResult.currentPage());
        Assertions.assertEquals(1, actualResult.perPage());
        Assertions.assertEquals(0, actualResult.total());
        Assertions.assertEquals(0, actualResult.items().size());

    }

    @Test
    public void givenFollowingPagination_whenCallsFindAllWithPage1_shouldReturnPaginated(){
        final var expectedPage = 1;
        final var expectedPerPage = 1;
        final var expectedTotal = 1;

        final var filmes1 = Category.newCategory("Filmes", "Filmes", true);
        final var filmes2 = Category.newCategory("Filmes2", "Filmes2", true);
        final var filmes3 = Category.newCategory("Filmes3", "Filmes3", true);

        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAll(List.of(CategoryJpaEntity.from(filmes1), CategoryJpaEntity.from(filmes2), CategoryJpaEntity.from(filmes3)));

        Assertions.assertEquals(3, categoryRepository.count());

        final var query = new CategorySearchQuery(1, 1, "2", "name", "asc");
        final var actualResult = categoryGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(filmes2.getId(), actualResult.items().get(0).getId());
    }

}

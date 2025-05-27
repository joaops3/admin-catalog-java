package com.admin.catalogo.e2e.category;


import com.admin.catalogo.E2ETest;
import com.admin.catalogo.domain.category.CategoryID;
import com.admin.catalogo.infrastructure.category.models.CategoryApiResponse;
import com.admin.catalogo.infrastructure.category.models.CreateCategoryApiRequest;
import com.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import com.admin.catalogo.infrastructure.configuration.Json;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@E2ETest
@Testcontainers
public class CategoryE2ETest {


    @Autowired
    private MockMvc mvc;

    @Autowired
    private CategoryRepository categoryRepository;

    @Container
    private static final MySQLContainer MYSQL_CONTAINER = (MySQLContainer) new MySQLContainer("mysql:latest").withUsername("root")
            .withPassword("123456")
            .withDatabaseName("adm_videos")
            .withReuse(true);

    @DynamicPropertySource
    public static void setDatasourceProperties(final DynamicPropertyRegistry registry){
        final var mappedPort = MYSQL_CONTAINER.getMappedPort(3306);

        System.out.printf("Container running on %s", mappedPort);
        registry.add("mysql.port", () -> mappedPort);
    }

    @Test
    public void testWorks(){
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
    }

    @Test
    public void asACatalogAdmingIShouldBeAbleToCreateANewCategory() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        // Given
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;


        // When
        final var actualId = givenACategory(expectedName, expectedDescription, expectedIsActive);

        final var actualCategory = retrieveCategory(actualId.getValue());
        // Then

        Assertions.assertEquals(expectedName, actualCategory.name());
        Assertions.assertEquals(expectedDescription, actualCategory.description());
        Assertions.assertEquals(expectedIsActive, actualCategory.active());
        Assertions.assertNotNull(actualCategory.created_at());
        Assertions.assertNotNull(actualCategory.updated_at());
        Assertions.assertNull(actualCategory.deleted_at());

    }


    @Test
    public void asACatalogAdmingIShouldBeAbleToGetCategoryById() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        // Given
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;


        // When
        final var actualId = givenACategory(expectedName, expectedDescription, expectedIsActive);

        final var actualCategory = retrieveCategory(actualId.getValue());
        // Then

        Assertions.assertEquals(expectedName, actualCategory.name());
        Assertions.assertEquals(expectedDescription, actualCategory.description());
        Assertions.assertEquals(expectedIsActive, actualCategory.active());
        Assertions.assertNotNull(actualCategory.created_at());
        Assertions.assertNotNull(actualCategory.updated_at());
        Assertions.assertNull(actualCategory.deleted_at());

    }

    @Test
    public void asACatalogAdmingIShouldBeAbleToSeeATreatedErrorByGettingANotFoundCategory() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());

        final var request = MockMvcRequestBuilders
                .get("/categories/123")
                .contentType(MediaType.APPLICATION_JSON_VALUE)


        final var aResponse = mvc.perform(request).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo("Category with ID 123 was not found")));

    }

    @Test
    public void asACatalogAdminIShouldBeAbleToNavigateToAllCategories() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());

        this.givenACategory("Filmes", "filmes desc", true);
        this.givenACategory("Series", "series desc", true);
        this.givenACategory("Documentarios", "documentarios desc", true);

        this.listCategories(0, 1).andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Filmes")))
                .andExpect(jsonPath("$.items[0].description", equalTo("filmes desc")));

    }

    private ResultActions listCategories(final int page, final int perPage, final String search , final String sort, final String direction) throws Exception {
        final var request = MockMvcRequestBuilders
                .get("/categories")
                .queryParam("page", String.valueOf(page))
                .queryParam("per_page", String.valueOf(perPage))
                .queryParam("search", search)
                .queryParam("sort", sort)
                .queryParam("direction", direction)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE);
        return this.mvc.perform(request);
    }

    private ResultActions listCategories(final int page, final int perPage) throws Exception {

        return this.listCategories(page, perPage, "", "", "");
    }

    private CategoryID givenACategory(
            final String expectedName,
            final String expectedDescription,
            final boolean expectedIsActive
    ) throws Exception {
       final var body = new CreateCategoryApiRequest(
               expectedName,
               expectedDescription,
               expectedIsActive
       );

       final var request = MockMvcRequestBuilders
                .post("/categories")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(Json.writeValueAsString(body));

       final var aResponse = mvc.perform(request).andExpect(status().isCreated())
               .andReturn()
               .getResponse()
               .getHeader("Location")
               .replace("/categories/", "");

//            final var actualJson = mvc.perform(request).andExpect(status().isCreated())
//                    .andReturn()
//                    .getResponse().getContentAsString();
//
//            final var json = Json.readValue(actualJson, Map.class);


        return CategoryID.from(aResponse);
    }

    private CategoryApiResponse retrieveCategory(final String id) throws Exception {
        final var request = MockMvcRequestBuilders
                .get("/categories/" + id)
                .contentType(MediaType.APPLICATION_JSON_VALUE);

        final var actualJson = mvc.perform(request).andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return Json.readValue(actualJson, CategoryApiResponse.class);
    }
}

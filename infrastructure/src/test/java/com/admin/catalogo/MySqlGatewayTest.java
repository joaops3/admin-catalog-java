package com.admin.catalogo;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.annotation.*;
import java.util.Collection;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ActiveProfiles("test-integration")
@DataJpaTest
@ComponentScan(includeFilters = {
        @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".[MySqlGateway]")
})
@ExtendWith(MySqlGatewayTest.CleanUpException.class)
public @interface MySqlGatewayTest {


    static class CleanUpException implements BeforeEachCallback {
        @Override
        public void beforeEach(ExtensionContext extensionContext) throws Exception {
            final var repositories = SpringExtension.getApplicationContext(extensionContext)
                    .getBeansOfType(CrudRepository.class).values();

            cleanUp(repositories);

        }

        private void cleanUp(final Collection<CrudRepository> repo){
            repo.forEach(CrudRepository::deleteAll);
        }
    }

}
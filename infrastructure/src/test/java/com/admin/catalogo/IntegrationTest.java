package com.admin.catalogo;

import com.admin.catalogo.infrastructure.configuration.WebServerConfig;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.annotation.*;
import java.util.Collection;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ActiveProfiles("test-integration")
@SpringBootTest(classes = WebServerConfig.class)
@ExtendWith(IntegrationTest.CleanUpException.class)
public @interface IntegrationTest {


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
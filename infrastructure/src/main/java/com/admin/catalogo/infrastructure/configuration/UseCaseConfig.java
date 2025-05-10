package com.admin.catalogo.infrastructure.configuration;

import com.admin.catalogo.application.category.create.CreateCategoryUseCase;
import com.admin.catalogo.application.category.create.DefaultCreateCategoryUseCase;
import com.admin.catalogo.application.category.delete.DefaultDeleteCategoryUseCase;
import com.admin.catalogo.application.category.delete.DeleteCategoryUseCase;
import com.admin.catalogo.application.category.retrieve.get.DefaultCategoryByIdUseCase;
import com.admin.catalogo.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.admin.catalogo.application.category.update.DefaultUpdateCategoryUseCase;
import com.admin.catalogo.application.category.update.UpdateCategoryUseCase;
import com.admin.catalogo.domain.category.CategoryGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {


    private final CategoryGateway categoryGateway;

    public UseCaseConfig(CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }

    @Bean
    public CreateCategoryUseCase createCategoryUseCase(){
        return new DefaultCreateCategoryUseCase(this.categoryGateway);
    }

    @Bean
    public UpdateCategoryUseCase updateCategoryUseCase(){
        return new DefaultUpdateCategoryUseCase(this.categoryGateway);
    }

    @Bean
    public GetCategoryByIdUseCase getByIdCategoryUseCase(){
        return new DefaultCategoryByIdUseCase(this.categoryGateway);
    }

    @Bean
    public DeleteCategoryUseCase deleteCategoryUseCase(){
        return new DefaultDeleteCategoryUseCase(this.categoryGateway);
    }
}

package com.admin.catalogo.application.category.update;

public record UpdateCategoryCommand(String id, String name, String description, boolean status) {

    public static UpdateCategoryCommand with(String id, String aname, String adescription, boolean astatus) {
        return new UpdateCategoryCommand(id, aname, adescription, astatus);
    }

}

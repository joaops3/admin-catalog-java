package com.admin.catalogo.domain.pagination;

import java.util.List;

public record Pagination<T>(int currentPage, int perPage, long total, List<T> items  ) {


    public boolean isLastPage() {
        return currentPage >= total;
    }

    public boolean hasNextPage() {
        return currentPage < total;
    }

    public boolean hasPreviousPage() {
        return currentPage > 1;
    }
}

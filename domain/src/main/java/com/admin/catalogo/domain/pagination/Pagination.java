package com.admin.catalogo.domain.pagination;

import java.util.List;
import java.util.function.Function;

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

    public <R> Pagination<R> map(final Function<T, R> mapper) {
       List<R> list = this.items.stream().map(mapper).toList();

       return new Pagination<>(
               this.currentPage,
               this.perPage,
               this.total,
               list
       );

    }
}

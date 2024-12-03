package com.mercadolibre.coupon.application.inbound.mercadolibre.filter;

import com.mercadolibre.coupon.application.inbound.mercadolibre.ProductFilter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class FilterChain<T> {

    @Builder.Default
    private final List<ProductFilter<T>> filters = new ArrayList<>();

    public FilterChain<T> addFilter(ProductFilter<T> filter) {
        filters.add(filter);
        return this;
    }

    public Mono<T> applyFilters(Mono<T> object) {
        Mono<T> result = object;

        for (ProductFilter<T> filter : filters) {
            result = result.flatMap(coupon -> filter.applyFilter(Mono.just(coupon)));
        }

        return result;
    }

}

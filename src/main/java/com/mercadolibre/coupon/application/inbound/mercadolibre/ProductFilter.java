package com.mercadolibre.coupon.application.inbound.mercadolibre;

import reactor.core.publisher.Mono;

public interface ProductFilter<T> {

    Mono<T> applyFilter(final Mono<T> t);

}

package com.mercadolibre.coupon.domain.context;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public enum MessageContextMercadoLibre {

    // Coupon
    COUPON,
    PRODUCTS,
    PRODUCTS_BY_COUPON,

    // Product
    LIMIT,
    COUNTRY,
    COUNTRIES,
    PRODUCTS_REDEEMED

}

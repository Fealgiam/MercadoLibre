package com.mercadolibre.coupon.domain.context;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public enum MessageContextMercadoLibre {

    // Coupon
    COUPON,
    PRODUCTS_BY_COUNTRY,
    COUPONS_BY_COUNTRY,
    COUPONS_PROCESS,

    // Product
    LIMIT,
    COUNTRY,
    COUNTRIES,
    PRODUCTS_REDEEMED

}

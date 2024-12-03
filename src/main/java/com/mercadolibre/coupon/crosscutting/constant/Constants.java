package com.mercadolibre.coupon.crosscutting.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Constants {

    /**
     * Name Caches
     */
    public static final String NAME_CACHE_CALL_API_COUNTRY = "country";
    public static final String NAME_CACHE_CALL_API_COUNTRIES = "countries";

    public static final List<String> CACHE_CALL_APIS = List
            .of(NAME_CACHE_CALL_API_COUNTRY, NAME_CACHE_CALL_API_COUNTRIES);

}

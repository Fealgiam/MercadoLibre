package com.mercadolibre.coupon.crosscutting.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResourceEndpoints {

    /**
     * Custom headers
     */
    public static final String X_API_VERSION = "x-api-version";
    public static final String X_API_VERSION_V1 = X_API_VERSION + "=v1";

    /**
     * Custom params
     */
    public static final String COUNTRY_CODE_PARAM = "country_code";
    public static final String NUMBER_RECORD_PARAM = "number_record_param";

    /**
     * Endpoints
     */
    public static final String COUPON_PATH = "/coupon";

    public static final String COUPON_PRODUCTS_PATH = "/coupon/products";
    public static final String COUPON_PRODUCTS_COUNTRY_PATH = "/{country_code}";

}

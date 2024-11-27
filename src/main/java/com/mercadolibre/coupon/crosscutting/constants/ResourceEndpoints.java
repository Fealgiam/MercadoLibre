package com.mercadolibre.coupon.crosscutting.constants;

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
     * Endpoints
     */
    public static final String COUPON_PATH = "/coupon";

}

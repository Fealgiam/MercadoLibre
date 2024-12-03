package com.mercadolibre.coupon.infrastructure.model.entrypoint.coupon;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class CouponRqRs {

    @Getter
    @JsonIgnore
    protected String version;

}

package com.mercadolibre.coupon.infrastructure.model.entrypoint.Product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class ProductRqRs {

    @Getter
    @JsonIgnore
    protected String version;

}

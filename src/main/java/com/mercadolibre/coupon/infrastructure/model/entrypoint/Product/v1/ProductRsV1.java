package com.mercadolibre.coupon.infrastructure.model.entrypoint.Product.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mercadolibre.coupon.infrastructure.model.entrypoint.Product.ProductRqRs;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ProductRsV1 extends ProductRqRs implements Serializable {

    @Serial
    private static final long serialVersionUID = -2261304200296649134L;

    {
        this.version = "v1";
    }

    @JsonProperty("items_redeemed")
    private Map<String, Long> itemsRedeemed;

}

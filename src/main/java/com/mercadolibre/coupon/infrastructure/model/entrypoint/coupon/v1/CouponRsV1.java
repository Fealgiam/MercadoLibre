package com.mercadolibre.coupon.infrastructure.model.entrypoint.coupon.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mercadolibre.coupon.infrastructure.model.entrypoint.coupon.CouponRqRs;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CouponRsV1 extends CouponRqRs implements Serializable {

    @Serial
    private static final long serialVersionUID = -1866175730182655777L;

    {
        this.version = "v1";
    }

    private Double total;

    @JsonProperty("item_ids")
    private List<String> items;

}

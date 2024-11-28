package com.mercadolibre.coupon.infrastructure.model.entrypoint.coupon.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mercadolibre.coupon.infrastructure.model.entrypoint.coupon.CouponRqRs;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Set;

import static com.mercadolibre.coupon.crosscutting.constant.MessageKeys.MSJ_RQ_VAL_AMOUNT_NULL;
import static com.mercadolibre.coupon.crosscutting.constant.MessageKeys.MSJ_RQ_VAL_AMOUNT_POSITIVE;
import static com.mercadolibre.coupon.crosscutting.constant.MessageKeys.MSJ_RQ_VAL_ITEMS_EMPTY;
import static com.mercadolibre.coupon.crosscutting.constant.MessageKeys.MSJ_RQ_VAL_ITEMS_NULL;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CouponV1Rq extends CouponRqRs implements Serializable {

    @Serial
    private static final long serialVersionUID = 5065448638242351175L;

    {
        this.version = "v1";
    }

    @NotNull(message = MSJ_RQ_VAL_AMOUNT_NULL)
    @Positive(message = MSJ_RQ_VAL_AMOUNT_POSITIVE)
    private Double amount;

    @JsonProperty("item_ids")
    @NotNull(message = MSJ_RQ_VAL_ITEMS_NULL)
    @NotEmpty(message = MSJ_RQ_VAL_ITEMS_EMPTY)
    private Set<String> items;

}

package com.mercadolibre.coupon.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "code")
public class Country {

    @Builder.Default
    private String code = StringUtils.EMPTY;

    @Builder.Default
    private String name = StringUtils.EMPTY;

    @Builder.Default
    private String currency = StringUtils.EMPTY;

}

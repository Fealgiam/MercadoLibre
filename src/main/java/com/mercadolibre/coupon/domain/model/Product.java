package com.mercadolibre.coupon.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@ToString
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Getter
    @Setter
    private String id;

    @Getter
    @Setter
    @Builder.Default
    private Double price = Double.valueOf(0);

    @Getter
    @Builder.Default
    private Integer priceConversion = Integer.valueOf(1);

    @Getter
    @Setter
    @Builder.Default
    private Country country = Country.builder().build();

    @Getter
    @Setter
    @Builder.Default
    private Integer timesRedeemed = Integer.valueOf(0);

}

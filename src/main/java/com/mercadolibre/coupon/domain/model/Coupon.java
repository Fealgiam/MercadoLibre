package com.mercadolibre.coupon.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {

    @Getter
    @Setter
    @Builder.Default
    private Double amount = Double.valueOf(0);

    @Getter
    @Setter
    @Builder.Default
    private Boolean applyFilter = Boolean.TRUE;

    @Getter
    @Setter
    @Builder.Default
    private Double amountRedeemable = Double.valueOf(0);

    @Getter
    private Integer amountConversion = Integer.valueOf(1);

    @Getter
    @Setter
    @Builder.Default
    private List<Product> products = new ArrayList<>();

    @Getter
    @Setter
    @Builder.Default
    private List<Product> productsRedeemable = new ArrayList<>();

}

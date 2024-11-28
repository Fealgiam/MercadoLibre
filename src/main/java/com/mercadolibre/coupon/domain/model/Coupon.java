package com.mercadolibre.coupon.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Coupon {

    private Double amount;

    private Double amountRedeemable;

    private Set<Product> products;

    private Set<Product> productsRedeemable;

}

package com.mercadolibre.coupon.infrastructure.mapper;

import com.mercadolibre.coupon.domain.model.Coupon;
import com.mercadolibre.coupon.domain.model.Product;
import com.mercadolibre.coupon.infrastructure.model.entrypoint.coupon.v1.CouponRsV1;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@NoArgsConstructor
public class CouponRsV1Mapper {

    public CouponRsV1 mapper(Coupon couponRedeemable) {
        return Optional.ofNullable(couponRedeemable)
                .map(coupon -> CouponRsV1
                        .builder()
                        .items(getIdProductsRedeemable(coupon))
                        .total(coupon.getAmountRedeemable())
                        .build())
                .orElse(CouponRsV1.builder().build());
    }

    // Private Methods
    private Set<String> getIdProductsRedeemable(Coupon couponRedeemable) {
        return couponRedeemable
                .getProductsRedeemable()
                .stream()
                .map(Product::getId)
                .collect(Collectors.toSet());
    }

}

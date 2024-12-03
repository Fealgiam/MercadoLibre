package com.mercadolibre.coupon.infrastructure.mapper;

import com.mercadolibre.coupon.domain.model.Coupon;
import com.mercadolibre.coupon.domain.model.Product;
import com.mercadolibre.coupon.infrastructure.model.entrypoint.coupon.v1.CouponRsV1;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

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
    private List<String> getIdProductsRedeemable(Coupon couponRedeemable) {
        return couponRedeemable
                .getProductsRedeemable()
                .stream()
                .map(Product::getId)
                .toList();
    }

}

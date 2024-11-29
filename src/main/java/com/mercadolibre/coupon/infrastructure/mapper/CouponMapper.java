package com.mercadolibre.coupon.infrastructure.mapper;

import com.mercadolibre.coupon.domain.model.Coupon;
import com.mercadolibre.coupon.infrastructure.model.entrypoint.coupon.v1.CouponRqV1;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CouponMapper {

    private final ProductMapper productMapper;


    public Coupon mapper(final CouponRqV1 couponsV1Rq) {
        return Optional.ofNullable(couponsV1Rq)
                .map(couponsRq -> Coupon
                        .builder()
                        .amount(couponsRq.getAmount())
                        .products(productMapper.mapperFromIds(couponsRq.getItems()))
                        .build())
                .orElse(Coupon.builder().build());
    }

}

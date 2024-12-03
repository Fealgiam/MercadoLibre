package com.mercadolibre.coupon.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.coupon.domain.model.Product;
import com.mercadolibre.coupon.infrastructure.model.entrypoint.coupon.v1.CouponRqV1;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class MockUtils {

    public static final String COUNTRY_CODE = "MCO";
    public static final Optional<Integer> LIMIT = Optional.of(5);

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static final List<Product> getProducts() {
        return List.of(
                Product.builder().id("MCO2669173224").timesRedeemed(1).build(),
                Product.builder().id("MCO1466867097").timesRedeemed(2).build(),
                Product.builder().id("MCO1419597389").timesRedeemed(3).build(),
                Product.builder().id("MCO1493507143").timesRedeemed(4).build(),
                Product.builder().id("MCO1480838649").timesRedeemed(5).build()
        );
    }

    public static final CouponRqV1 getCouponRqV1() {
        return CouponRqV1
                .builder()
                .amount(900000.01)
                .items(getItemIds())
                .build();
    }

    public static final CouponRqV1 getCouponRqV1NegativeAmount() {
        return CouponRqV1
                .builder()
                .amount(-1.01)
                .items(getItemIds())
                .build();
    }

    public static final CouponRqV1 getCouponRqV1InvalidAmount() {
        return CouponRqV1
                .builder()
                .amount(1.01)
                .items(getItemIds())
                .build();
    }

    public static final Set<String> getItemIds() {
        return Set.of("MCO1480838649", "MCO1493507143", "MCO1419597389", "MCO1466867097", "MCO2669173224");
    }

}

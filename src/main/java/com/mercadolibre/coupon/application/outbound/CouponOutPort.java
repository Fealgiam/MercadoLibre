package com.mercadolibre.coupon.application.outbound;

import com.mercadolibre.coupon.domain.model.Coupon;

public interface CouponOutPort {

    void saveCouponRedeemed(final Coupon coupon);

}

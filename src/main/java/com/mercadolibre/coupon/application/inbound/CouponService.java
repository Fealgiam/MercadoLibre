package com.mercadolibre.coupon.application.inbound;

import com.mercadolibre.coupon.domain.model.Coupon;

public interface CouponService {

    Coupon calculateProductsCoupon(final Coupon coupon);

}

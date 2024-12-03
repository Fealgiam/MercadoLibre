package com.mercadolibre.coupon.application.inbound;

import com.mercadolibre.coupon.domain.model.Coupon;

public interface CouponInPort {

    Coupon calculateBestOfferCoupon(final Coupon coupon);

}

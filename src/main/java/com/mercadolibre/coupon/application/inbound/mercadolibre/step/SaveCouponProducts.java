package com.mercadolibre.coupon.application.inbound.mercadolibre.step;

import com.mercadolibre.coupon.application.outbound.CouponProductService;
import com.mercadolibre.coupon.crosscutting.utility.PropagationExceptionUtility;
import com.mercadolibre.coupon.domain.context.MessageContext;
import com.mercadolibre.coupon.domain.context.MessageContextCoupon;
import com.mercadolibre.coupon.domain.model.Coupon;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.function.UnaryOperator;

import static com.mercadolibre.coupon.crosscutting.constant.MessageKeys.MSJ_GEN_FOR_SUM_ERROR;
import static com.mercadolibre.coupon.crosscutting.utility.MessageUtility.getMessage;
import static com.mercadolibre.coupon.domain.context.MessageContextCoupon.COUPON;
import static java.lang.String.format;

@Log4j2
@Component
@RequiredArgsConstructor
public class SaveCouponProducts implements UnaryOperator<MessageContext<MessageContextCoupon, Object>> {

    private static final String CLASS_NAME = SaveCouponProducts.class.getSimpleName();

    // Services
    private final CouponProductService couponProductService;


    @Override
    public MessageContext<MessageContextCoupon, Object> apply(
            final MessageContext<MessageContextCoupon, Object> context) {
        try {
            // load properties
            final var couponRedeemed = context.getItem(COUPON, Coupon.class);

            // Save products redeemed for the coupon value
            couponProductService.saveCouponRedeemed(couponRedeemed);

            return context;
        } catch (Exception ex) {
            log.error(format(getMessage(MSJ_GEN_FOR_SUM_ERROR), CLASS_NAME, "apply"));
            throw PropagationExceptionUtility.generateMercadoLibreException(ex);
        }
    }

}

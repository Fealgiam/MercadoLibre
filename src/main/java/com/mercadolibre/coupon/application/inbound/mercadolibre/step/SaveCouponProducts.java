package com.mercadolibre.coupon.application.inbound.mercadolibre.step;

import com.mercadolibre.coupon.application.outbound.CouponOutPort;
import com.mercadolibre.coupon.crosscutting.exception.business.BusinessException;
import com.mercadolibre.coupon.crosscutting.exception.technical.TechnicalException;
import com.mercadolibre.coupon.crosscutting.utility.PropagationExceptionUtility;
import com.mercadolibre.coupon.domain.context.MessageContext;
import com.mercadolibre.coupon.domain.context.MessageContextMercadoLibre;
import com.mercadolibre.coupon.domain.model.Coupon;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.time.Duration;
import java.util.function.UnaryOperator;

import static com.mercadolibre.coupon.crosscutting.constant.MessageKeys.MSJ_BUSINESS_COUPON_INAPPLICABLE;
import static com.mercadolibre.coupon.crosscutting.constant.MessageKeys.MSJ_GEN_FOR_SUM_ERROR;
import static com.mercadolibre.coupon.crosscutting.utility.MessageUtility.getMessage;
import static com.mercadolibre.coupon.domain.context.MessageContextMercadoLibre.COUPON;
import static com.mercadolibre.coupon.domain.context.MessageContextMercadoLibre.COUPONS_PROCESS;
import static java.lang.String.format;

@Log4j2
@Component
@RequiredArgsConstructor
public class SaveCouponProducts implements UnaryOperator<MessageContext<MessageContextMercadoLibre, Object>> {

    private static final String CLASS_NAME = SaveCouponProducts.class.getSimpleName();

    // Services
    private final CouponOutPort couponOutPort;


    @Override
    @SuppressWarnings("unchecked")
    public MessageContext<MessageContextMercadoLibre, Object> apply(
            final MessageContext<MessageContextMercadoLibre, Object> context) {
        try {
            // load properties
            final var coupon = context.getItem(COUPON, Coupon.class);
            final Sinks.One<Coupon> couponProcessor = context.getItem(COUPONS_PROCESS, Sinks.One.class);

            // Save products redeemed for the coupon value
            couponProcessor
                    .asMono()
                    .timeout(Duration.ofMinutes(2))
                    .doOnError(err -> Mono.error(new TechnicalException(err.getMessage())))
                    .doOnTerminate(() -> log.debug("Flow terminated"))
                    .blockOptional()
                    .ifPresent(bestCoupon -> this.validateResponseAndSaveCouponRedeemed(coupon, bestCoupon));

            return context;
        } catch (Exception ex) {
            log.error(format(getMessage(MSJ_GEN_FOR_SUM_ERROR), CLASS_NAME, "apply"));
            throw PropagationExceptionUtility.generateMercadoLibreException(ex);
        }
    }

    // Private methods
    private void validateResponseAndSaveCouponRedeemed(final Coupon coupon, final Coupon calculate) {
        if (calculate.getProductsRedeemable().isEmpty()) {
            throw new BusinessException(getMessage(MSJ_BUSINESS_COUPON_INAPPLICABLE));
        } else {
            coupon.setAmountRedeemable(calculate.getAmountRedeemable());
            coupon.setProductsRedeemable(calculate.getProductsRedeemable());

            couponOutPort.saveCouponRedeemed(coupon);
        }
    }

}

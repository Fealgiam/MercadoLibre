package com.mercadolibre.coupon.application.inbound.mercadolibre.step;

import com.mercadolibre.coupon.crosscutting.utility.PropagationExceptionUtility;
import com.mercadolibre.coupon.domain.context.MessageContext;
import com.mercadolibre.coupon.domain.context.MessageContextMercadoLibre;
import com.mercadolibre.coupon.domain.model.Coupon;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.Map;
import java.util.function.UnaryOperator;

import static com.mercadolibre.coupon.crosscutting.constant.MessageKeys.MSJ_GEN_FOR_SUM_ERROR;
import static com.mercadolibre.coupon.crosscutting.utility.MessageUtility.getMessage;
import static com.mercadolibre.coupon.domain.context.MessageContextMercadoLibre.COUPON;
import static com.mercadolibre.coupon.domain.context.MessageContextMercadoLibre.PRODUCTS_BY_COUPON;
import static java.lang.String.format;

@Log4j2
@Component
@RequiredArgsConstructor
public class SelectBestOfferCoupon implements UnaryOperator<MessageContext<MessageContextMercadoLibre, Object> > {

    private static final String CLASS_NAME = SelectBestOfferCoupon.class.getSimpleName();


    @Override
    public MessageContext<MessageContextMercadoLibre, Object>  apply(
            final MessageContext<MessageContextMercadoLibre, Object>  context) {
        try {
            // load properties
            @SuppressWarnings("unchecked")
            final Map<String, Coupon> bestOfferByCountry = context.getItem(PRODUCTS_BY_COUPON, Map.class);

            // Get best offer
            final var coupon = bestOfferByCountry
                    .values()
                    .stream()
                    .max(Comparator.comparing(Coupon::getAmountRedeemable))
                    .orElse(null);

            // Add context
            context.addItem(COUPON, coupon);

            return context;
        } catch (Exception ex) {
            log.error(format(getMessage(MSJ_GEN_FOR_SUM_ERROR), CLASS_NAME, "apply"));
            throw PropagationExceptionUtility.generateMercadoLibreException(ex);
        }
    }

}
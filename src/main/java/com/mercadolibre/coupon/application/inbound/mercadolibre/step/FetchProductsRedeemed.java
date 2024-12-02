package com.mercadolibre.coupon.application.inbound.mercadolibre.step;

import com.mercadolibre.coupon.application.outbound.ProductRedeemedOutPort;
import com.mercadolibre.coupon.crosscutting.utility.PropagationExceptionUtility;
import com.mercadolibre.coupon.domain.context.MessageContext;
import com.mercadolibre.coupon.domain.context.MessageContextMercadoLibre;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.UnaryOperator;

import static com.mercadolibre.coupon.crosscutting.constant.MessageKeys.MSJ_GEN_FOR_SUM_ERROR;
import static com.mercadolibre.coupon.crosscutting.utility.MessageUtility.getMessage;
import static com.mercadolibre.coupon.domain.context.MessageContextMercadoLibre.LIMIT;
import static com.mercadolibre.coupon.domain.context.MessageContextMercadoLibre.PRODUCTS_REDEEMED;
import static java.lang.String.format;

@Log4j2
@Component
@RequiredArgsConstructor
public class FetchProductsRedeemed implements UnaryOperator<MessageContext<MessageContextMercadoLibre, Object>> {

    private static final String CLASS_NAME = FetchProductsRedeemed.class.getSimpleName();

    @Value("${application.default-limit}")
    private Integer defaultLimit;

    // Services
    private final ProductRedeemedOutPort productRedeemedOutPort;


    @Override
    @SuppressWarnings("unchecked")
    public MessageContext<MessageContextMercadoLibre, Object> apply(
            final MessageContext<MessageContextMercadoLibre, Object> context) {
        try {
            // load properties
            final Optional<Integer> limit = context.getItem(LIMIT, Optional.class);

            // Fetch products redeemed by country
            var productsRedeemed = productRedeemedOutPort.fetchProductRedeemed(limit.orElse(defaultLimit));

            // Add to context
            context.addItem(PRODUCTS_REDEEMED, productsRedeemed);

            return context;
        } catch (Exception ex) {
            log.error(format(getMessage(MSJ_GEN_FOR_SUM_ERROR), CLASS_NAME, "apply"));
            throw PropagationExceptionUtility.generateMercadoLibreException(ex);
        }
    }

}

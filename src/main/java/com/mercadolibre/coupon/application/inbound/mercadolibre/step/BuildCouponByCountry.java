package com.mercadolibre.coupon.application.inbound.mercadolibre.step;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.coupon.crosscutting.utility.PropagationExceptionUtility;
import com.mercadolibre.coupon.domain.context.MessageContext;
import com.mercadolibre.coupon.domain.context.MessageContextMercadoLibre;
import com.mercadolibre.coupon.domain.model.Coupon;
import com.mercadolibre.coupon.domain.model.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

import static com.mercadolibre.coupon.crosscutting.constant.MessageKeys.MSJ_GEN_FOR_SUM_ERROR;
import static com.mercadolibre.coupon.crosscutting.utility.MessageUtility.getMessage;
import static com.mercadolibre.coupon.domain.context.MessageContextMercadoLibre.COUPON;
import static com.mercadolibre.coupon.domain.context.MessageContextMercadoLibre.COUPONS_BY_COUNTRY;
import static com.mercadolibre.coupon.domain.context.MessageContextMercadoLibre.PRODUCTS_BY_COUNTRY;
import static java.lang.String.format;

@Log4j2
@Component
@RequiredArgsConstructor
public class BuildCouponByCountry implements UnaryOperator<MessageContext<MessageContextMercadoLibre, Object>> {

    private static final String CLASS_NAME = BuildCouponByCountry.class.getSimpleName();

    // Mappers
    private final ObjectMapper mapper;


    @Override
    @SuppressWarnings("unchecked")
    public MessageContext<MessageContextMercadoLibre, Object> apply(
            final MessageContext<MessageContextMercadoLibre, Object> context) {
        try {
            // load properties
            final var coupon = context.getItem(COUPON, Coupon.class);
            final Map<String, List<Product>> productsByCountry = context.getItem(PRODUCTS_BY_COUNTRY, Map.class);

            // Build coupons by country
            final var couponsByCountry = new ArrayList<Mono<Coupon>>();
            for (final var productsCountry : productsByCountry.values()) {
                var couponCountry = mapper.readValue(mapper.writeValueAsString(coupon), Coupon.class);
                couponCountry.setProducts(productsCountry);
                couponsByCountry.add(Mono.just(couponCountry));
            }

            // Add context
            context.addItem(COUPONS_BY_COUNTRY, couponsByCountry);

            return context;
        } catch (Exception ex) {
            log.error(format(getMessage(MSJ_GEN_FOR_SUM_ERROR), CLASS_NAME, "apply"));
            throw PropagationExceptionUtility.generateMercadoLibreException(ex);
        }
    }

}
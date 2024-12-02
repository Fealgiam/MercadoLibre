package com.mercadolibre.coupon.application.inbound.mercadolibre.step;

import com.mercadolibre.coupon.application.outbound.ProductRedeemedOutPort;
import com.mercadolibre.coupon.crosscutting.exception.MercadoLibreException;
import com.mercadolibre.coupon.crosscutting.utility.PropagationExceptionUtility;
import com.mercadolibre.coupon.domain.context.MessageContext;
import com.mercadolibre.coupon.domain.context.MessageContextMercadoLibre;
import com.mercadolibre.coupon.domain.model.Country;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.UnaryOperator;

import static com.mercadolibre.coupon.crosscutting.constant.MessageKeys.MSJ_EX_CODE_BUSINESS;
import static com.mercadolibre.coupon.crosscutting.constant.MessageKeys.MSJ_GEN_FOR_SUM_ERROR;
import static com.mercadolibre.coupon.crosscutting.constant.MessageKeys.MSJ_RQ_VAL_COUNTRY;
import static com.mercadolibre.coupon.crosscutting.utility.MessageUtility.getMessage;
import static com.mercadolibre.coupon.domain.context.MessageContextMercadoLibre.COUNTRIES;
import static com.mercadolibre.coupon.domain.context.MessageContextMercadoLibre.COUNTRY;
import static com.mercadolibre.coupon.domain.context.MessageContextMercadoLibre.LIMIT;
import static com.mercadolibre.coupon.domain.context.MessageContextMercadoLibre.PRODUCTS_REDEEMED;
import static java.lang.String.format;

@Log4j2
@Component
@RequiredArgsConstructor
public class FetchProductsRedeemedByCountry
        implements UnaryOperator<MessageContext<MessageContextMercadoLibre, Object>> {

    private static final String CLASS_NAME = FetchProductsRedeemedByCountry.class.getSimpleName();

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
            final var country = context.getItem(COUNTRY, String.class);
            final Optional<Integer> limit = context.getItem(LIMIT, Optional.class);
            final Map<String, Country> countries = context.getItem(COUNTRIES, Map.class);

            // Apply validation
            final var countrySearch = countries.get(country);
            if (Objects.isNull(countrySearch)) {
                throw new MercadoLibreException(
                        getMessage(MSJ_EX_CODE_BUSINESS), getMessage(MSJ_RQ_VAL_COUNTRY), HttpStatus.NOT_FOUND);
            }

            // Fetch products redeemed by country
            var productsRedeemed = productRedeemedOutPort
                    .fetchProductRedeemedByCountry(countrySearch.getCode(), limit.orElse(defaultLimit));

            // Add to context
            context.addItem(PRODUCTS_REDEEMED, productsRedeemed);

            return context;
        } catch (Exception ex) {
            log.error(format(getMessage(MSJ_GEN_FOR_SUM_ERROR), CLASS_NAME, "apply"));
            throw PropagationExceptionUtility.generateMercadoLibreException(ex);
        }
    }

}

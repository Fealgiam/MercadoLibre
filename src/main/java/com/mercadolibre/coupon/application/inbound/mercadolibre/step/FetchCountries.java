package com.mercadolibre.coupon.application.inbound.mercadolibre.step;

import com.mercadolibre.coupon.application.outbound.CountryOutPort;
import com.mercadolibre.coupon.crosscutting.utility.PropagationExceptionUtility;
import com.mercadolibre.coupon.domain.context.MessageContext;
import com.mercadolibre.coupon.domain.context.MessageContextMercadoLibre;
import com.mercadolibre.coupon.domain.model.Country;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import static com.mercadolibre.coupon.crosscutting.constant.MessageKeys.MSJ_GEN_FOR_SUM_ERROR;
import static com.mercadolibre.coupon.crosscutting.utility.MessageUtility.getMessage;
import static com.mercadolibre.coupon.domain.context.MessageContextMercadoLibre.COUNTRIES;
import static java.lang.String.format;

@Log4j2
@Component

@RequiredArgsConstructor
public class FetchCountries implements UnaryOperator<MessageContext<MessageContextMercadoLibre, Object>> {

    private static final String CLASS_NAME = FetchCountries.class.getSimpleName();

    // Services
    private final CountryOutPort countryOutPort;


    @Override
    public MessageContext<MessageContextMercadoLibre, Object> apply(
            final MessageContext<MessageContextMercadoLibre, Object> context) {
        try {
            // Transform to Map
            final var countries =  countryOutPort
                    .fetchCountries()
                    .stream()
                    .collect(Collectors.toMap(Country::getCode, Function.identity()));

            // Add in context
            context.addItem(COUNTRIES, countries);

            return context;
        } catch (Exception ex) {
            log.error(format(getMessage(MSJ_GEN_FOR_SUM_ERROR), CLASS_NAME, "apply"));
            throw PropagationExceptionUtility.generateMercadoLibreException(ex);
        }
    }

}

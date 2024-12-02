package com.mercadolibre.coupon.application.inbound.mercadolibre;

import com.mercadolibre.coupon.application.inbound.ProductInPort;
import com.mercadolibre.coupon.application.inbound.mercadolibre.step.FetchCountries;
import com.mercadolibre.coupon.application.inbound.mercadolibre.step.FetchProductsRedeemed;
import com.mercadolibre.coupon.application.inbound.mercadolibre.step.FetchProductsRedeemedByCountry;
import com.mercadolibre.coupon.crosscutting.utility.PropagationExceptionUtility;
import com.mercadolibre.coupon.domain.context.MessageContext;
import com.mercadolibre.coupon.domain.context.MessageContextEnum;
import com.mercadolibre.coupon.domain.context.MessageContextMercadoLibre;
import com.mercadolibre.coupon.domain.model.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static com.mercadolibre.coupon.crosscutting.constant.MessageKeys.MSJ_GEN_FOR_SUM_ERROR;
import static com.mercadolibre.coupon.crosscutting.utility.MessageUtility.getMessage;
import static com.mercadolibre.coupon.domain.context.MessageContextMercadoLibre.COUNTRY;
import static com.mercadolibre.coupon.domain.context.MessageContextMercadoLibre.LIMIT;
import static com.mercadolibre.coupon.domain.context.MessageContextMercadoLibre.PRODUCTS_REDEEMED;
import static java.lang.String.format;

@Log4j2
@Service
@RequiredArgsConstructor
public class ProductService implements ProductInPort {

    private static final String CLASS_NAME = ProductService.class.getSimpleName();

    // Steps
    private final FetchCountries fetchCountries;
    private final FetchProductsRedeemed fetchProductsRedeemed;
    private final FetchProductsRedeemedByCountry fetchProductsRedeemedByCountry;


    // Private Methods
    private MessageContext<MessageContextMercadoLibre, Object> initialPipelineData(final Optional<Integer> limit,
                                                                                   final String... country) {
        var context = new MessageContextEnum<MessageContextMercadoLibre, Object>(MessageContextMercadoLibre.class);
        context.addItem(LIMIT, limit);

        if (Objects.nonNull(country) && country.length > 0) {
            context.addItem(COUNTRY, country[0]);
        }

        return context;
    }

    // Custom Methods
    @Override
    @SuppressWarnings("unchecked")
    public Set<Product> fetchRedeemedProducts(Optional<Integer> limit) {
        try {
            return fetchProductsRedeemed
                    .apply(this.initialPipelineData(limit))
                    .getItem(PRODUCTS_REDEEMED, Set.class);
        } catch (Exception ex) {
            log.error(format(getMessage(MSJ_GEN_FOR_SUM_ERROR), CLASS_NAME, "fetchRedeemedProducts"));
            throw PropagationExceptionUtility.generateMercadoLibreException(ex);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<Product> fetchRedeemedProductsByCountry(String country, Optional<Integer> limit) {
        try {
            return fetchCountries
                    .andThen(fetchProductsRedeemedByCountry)
                    .apply(this.initialPipelineData(limit, country))
                    .getItem(PRODUCTS_REDEEMED, Set.class);
        } catch (Exception ex) {
            log.error(format(getMessage(MSJ_GEN_FOR_SUM_ERROR), CLASS_NAME, "fetchRedeemedProductsByCountry"));
            throw PropagationExceptionUtility.generateMercadoLibreException(ex);
        }
    }

}

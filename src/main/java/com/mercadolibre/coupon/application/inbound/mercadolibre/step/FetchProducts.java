package com.mercadolibre.coupon.application.inbound.mercadolibre.step;

import com.mercadolibre.coupon.application.outbound.ProductOutPort;
import com.mercadolibre.coupon.crosscutting.utility.PropagationExceptionUtility;
import com.mercadolibre.coupon.domain.context.MessageContext;
import com.mercadolibre.coupon.domain.context.MessageContextMercadoLibre;
import com.mercadolibre.coupon.domain.model.Coupon;
import com.mercadolibre.coupon.domain.model.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import static com.mercadolibre.coupon.crosscutting.constant.MessageKeys.MSJ_GEN_FOR_SUM_ERROR;
import static com.mercadolibre.coupon.crosscutting.utility.MessageUtility.getMessage;
import static com.mercadolibre.coupon.domain.context.MessageContextMercadoLibre.COUPON;
import static com.mercadolibre.coupon.domain.context.MessageContextMercadoLibre.PRODUCTS_BY_COUNTRY;
import static java.lang.String.format;

@Log4j2
@Component
@RequiredArgsConstructor
public class FetchProducts implements UnaryOperator<MessageContext<MessageContextMercadoLibre, Object>> {

    private static final String CLASS_NAME = FetchProducts.class.getSimpleName();

    // Services
    private final ProductOutPort productOutPort;


    @Override
    public MessageContext<MessageContextMercadoLibre, Object> apply(
            final MessageContext<MessageContextMercadoLibre, Object> context) {
        try {
            // load properties
            final var coupon = context.getItem(COUPON, Coupon.class);
            final var productIds = coupon.getProducts().stream().map(Product::getId).toList();

            // Fetch products
            final var productsByCountry = productOutPort
                    .fetchProducts(productIds)
                    .stream()
                    .collect(Collectors.groupingBy(product -> product.getCountry().getCode(), Collectors.toList()));

            // Add to context
            context.addItem(PRODUCTS_BY_COUNTRY, productsByCountry);

            return context;
        } catch (Exception ex) {
            log.error(format(getMessage(MSJ_GEN_FOR_SUM_ERROR), CLASS_NAME, "apply"));
            throw PropagationExceptionUtility.generateMercadoLibreException(ex);
        }
    }

}

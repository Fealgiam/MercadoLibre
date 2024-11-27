package com.mercadolibre.coupon.application.inbound.mercadolibre.steps;

import com.mercadolibre.coupon.application.outbound.ProductService;
import com.mercadolibre.coupon.crosscutting.utility.PropagationExceptionUtility;
import com.mercadolibre.coupon.domain.context.MessageContext;
import com.mercadolibre.coupon.domain.context.MessageContextCoupon;
import com.mercadolibre.coupon.domain.model.Coupon;
import com.mercadolibre.coupon.domain.model.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import static com.mercadolibre.coupon.crosscutting.constants.MessageKeys.MSJ_GEN_FOR_SUM_ERROR;
import static com.mercadolibre.coupon.crosscutting.utility.MessageUtility.getMessage;
import static com.mercadolibre.coupon.domain.context.MessageContextCoupon.COUPON;
import static com.mercadolibre.coupon.domain.context.MessageContextCoupon.PRODUCTS;
import static java.lang.String.format;

@Log4j2
@Component
@RequiredArgsConstructor
public class FetchProducts implements UnaryOperator<MessageContext<Enum<MessageContextCoupon>, Object>> {

    private static final String CLASS_NAME = FetchProducts.class.getSimpleName();

    private final ProductService productService;


    @Override
    public MessageContext<Enum<MessageContextCoupon>, Object> apply(
            final MessageContext<Enum<MessageContextCoupon>, Object> context) {
        try {
            // load properties
            final var coupon = context.getItem(COUPON, Coupon.class);
            final var productIds = coupon.getProducts().stream().map(Product::getId).toList();

            // Fetch products
            final var productsByCountry = productService
                    .fetchProducts(productIds)
                    .stream()
                    .collect(Collectors.groupingBy(product -> product.getCountry().getCode(), Collectors.toSet()));

            // Add to context
            context.addItem(PRODUCTS, productsByCountry);

            return context;
        } catch (Exception ex) {
            log.error(format(getMessage(MSJ_GEN_FOR_SUM_ERROR), CLASS_NAME, "apply"));
            throw PropagationExceptionUtility.generateMercadoLibreException(ex);
        }
    }

}

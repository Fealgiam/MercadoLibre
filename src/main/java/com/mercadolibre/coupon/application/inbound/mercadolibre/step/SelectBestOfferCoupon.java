package com.mercadolibre.coupon.application.inbound.mercadolibre.step;

import com.mercadolibre.coupon.application.inbound.mercadolibre.filter.AllProductsValidFilter;
import com.mercadolibre.coupon.application.inbound.mercadolibre.filter.BruteForceForSmallListFilter;
import com.mercadolibre.coupon.application.inbound.mercadolibre.filter.FilterChain;
import com.mercadolibre.coupon.application.inbound.mercadolibre.filter.GreedyBruteForceForBigListFilter;
import com.mercadolibre.coupon.application.inbound.mercadolibre.filter.NonProductsValidFilter;
import com.mercadolibre.coupon.application.inbound.mercadolibre.filter.OnlyTwoProductsFilter;
import com.mercadolibre.coupon.application.inbound.mercadolibre.filter.ProductPriceEqualCouponAmountFilter;
import com.mercadolibre.coupon.application.inbound.mercadolibre.filter.RemoveProductLowestPriceFilter;
import com.mercadolibre.coupon.crosscutting.utility.PropagationExceptionUtility;
import com.mercadolibre.coupon.domain.context.MessageContext;
import com.mercadolibre.coupon.domain.context.MessageContextMercadoLibre;
import com.mercadolibre.coupon.domain.model.Coupon;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.util.List;
import java.util.function.UnaryOperator;

import static com.mercadolibre.coupon.crosscutting.constant.MessageKeys.MSJ_GEN_FOR_SUM_ERROR;
import static com.mercadolibre.coupon.crosscutting.utility.MessageUtility.getMessage;
import static com.mercadolibre.coupon.domain.context.MessageContextMercadoLibre.COUPONS_BY_COUNTRY;
import static com.mercadolibre.coupon.domain.context.MessageContextMercadoLibre.COUPONS_PROCESS;
import static java.lang.String.format;

@Log4j2
@Component
@RequiredArgsConstructor
public class SelectBestOfferCoupon implements UnaryOperator<MessageContext<MessageContextMercadoLibre, Object>> {

    private static final String CLASS_NAME = SelectBestOfferCoupon.class.getSimpleName();

    // Filters
    private final NonProductsValidFilter nonProductsValidFilter;
    private final AllProductsValidFilter allProductsValidFilter;
    private final OnlyTwoProductsFilter onlyTwoProductsFilter;
    private final ProductPriceEqualCouponAmountFilter productPriceEqualCouponAmountFilter;
    private final RemoveProductLowestPriceFilter removeProductLowestPriceFilter;
    private final BruteForceForSmallListFilter bruteForceForSmallListFilter;
    private final GreedyBruteForceForBigListFilter greedyBruteForceForBigListFilter;


    @Override
    @SuppressWarnings("unchecked")
    public MessageContext<MessageContextMercadoLibre, Object> apply(
            final MessageContext<MessageContextMercadoLibre, Object> context) {
        try {
            // load properties
            final var couponProcess = Sinks.one();
            final List<Mono<Coupon>> couponsByCountry = context.getItem(COUPONS_BY_COUNTRY, List.class);

            // Calculate better offer by each coupon
            Flux.fromIterable(couponsByCountry)
                    .flatMap(couponCountry -> couponCountry.flatMap(this::calculateBetterOffer))
                    .reduce(((c1, c2) -> c1.getAmountRedeemable() > c2.getAmountRedeemable() ? c1 : c2))
                    .subscribe(couponProcess::tryEmitValue, couponProcess::tryEmitError);

            // Add into context
            context.addItem(COUPONS_PROCESS, couponProcess);

            return context;
        } catch (Exception ex) {
            log.error(format(getMessage(MSJ_GEN_FOR_SUM_ERROR), CLASS_NAME, "apply"));
            throw PropagationExceptionUtility.generateMercadoLibreException(ex);
        }
    }

    // Private Methods
    private Mono<Coupon> calculateBetterOffer(Coupon coupon) {
        return FilterChain
                .<Coupon>builder()
                .build()
                .addFilter(nonProductsValidFilter)
                .addFilter(allProductsValidFilter)
                .addFilter(onlyTwoProductsFilter)
                .addFilter(productPriceEqualCouponAmountFilter)
                .addFilter(removeProductLowestPriceFilter)
                .addFilter(bruteForceForSmallListFilter)
                .addFilter(greedyBruteForceForBigListFilter)
                .applyFilters(Mono.just(coupon));
    }

}
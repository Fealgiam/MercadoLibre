package com.mercadolibre.coupon.application.inbound.mercadolibre.filter;

import com.mercadolibre.coupon.application.inbound.mercadolibre.ProductFilter;
import com.mercadolibre.coupon.domain.model.Coupon;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class OnlyTwoProductsFilter implements ProductFilter<Coupon> {

    @Override
    public Mono<Coupon> applyFilter(final Mono<Coupon> coupon) {
        return coupon.flatMap(this::filterOnlyTwoProducts);
    }

    // Private Method
    private Mono<Coupon> filterOnlyTwoProducts(final Coupon coupon) {
        if (coupon.getApplyFilter() && coupon.getProductsRedeemable().size() == 2) {
            final var productsToRedeem = coupon.getProductsRedeemable().stream().toList();
            final var productMaxPrice = productsToRedeem.get(productsToRedeem.size() - 1);

            coupon.setProductsRedeemable(List.of(productMaxPrice));
            coupon.setAmountRedeemable(productMaxPrice.getPrice());
        }

        return Mono.just(coupon);
    }

}

package com.mercadolibre.coupon.application.inbound.mercadolibre.filter;

import com.mercadolibre.coupon.application.inbound.mercadolibre.ProductFilter;
import com.mercadolibre.coupon.domain.model.Coupon;
import com.mercadolibre.coupon.domain.model.Product;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class AllProductsValidFilter implements ProductFilter<Coupon> {

    @Override
    public Mono<Coupon> applyFilter(final Mono<Coupon> coupon) {
        return coupon.flatMap(this::filterAllProductsValid);
    }

    // Private Method
    private Mono<Coupon> filterAllProductsValid(final Coupon coupon) {
        if (coupon.getApplyFilter()) {
            final var productsToRedeem = coupon.getProductsRedeemable().isEmpty()
                    ? coupon.getProducts().stream().toList()
                    : coupon.getProductsRedeemable().stream().toList();

            final var sumTotalPrice = productsToRedeem.stream().mapToInt(Product::getPriceConversion).sum();

            if (coupon.getAmountConversion().intValue() >= sumTotalPrice) {
                coupon.setApplyFilter(Boolean.FALSE);
                coupon.setProductsRedeemable(productsToRedeem);
                coupon.setAmountRedeemable(productsToRedeem.stream().mapToDouble(Product::getPrice).sum());
            }
        }

        return Mono.just(coupon);
    }

}

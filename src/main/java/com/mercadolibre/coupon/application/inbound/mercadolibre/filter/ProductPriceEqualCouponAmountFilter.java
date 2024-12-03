package com.mercadolibre.coupon.application.inbound.mercadolibre.filter;

import com.mercadolibre.coupon.application.inbound.mercadolibre.ProductFilter;
import com.mercadolibre.coupon.domain.model.Coupon;
import com.mercadolibre.coupon.domain.model.Product;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Component
public class ProductPriceEqualCouponAmountFilter implements ProductFilter<Coupon> {

    @Override
    public Mono<Coupon> applyFilter(final Mono<Coupon> coupon) {
        return coupon.flatMap(this::filterProductPriceEqualCouponAmount);
    }

    // Private Method
    private Mono<Coupon> filterProductPriceEqualCouponAmount(final Coupon coupon) {
        if (coupon.getApplyFilter()) {
            final var productsToRedeem = coupon.getProductsRedeemable().isEmpty()
                    ? new ArrayList<Product>(coupon.getProducts())
                    : new ArrayList<Product>(coupon.getProductsRedeemable());

            productsToRedeem.sort(Comparator.comparingInt(Product::getPriceConversion));

            final var productEq = productsToRedeem.get(productsToRedeem.size() - 1);

            if (coupon.getAmountConversion() == productEq.getPriceConversion()) {
                coupon.setApplyFilter(Boolean.FALSE);
                coupon.setProductsRedeemable(List.of(productEq));
                coupon.setAmountRedeemable(productEq.getPrice());
            }
        }

        return Mono.just(coupon);
    }

}

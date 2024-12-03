package com.mercadolibre.coupon.application.inbound.mercadolibre.filter;

import com.mercadolibre.coupon.application.inbound.mercadolibre.ProductFilter;
import com.mercadolibre.coupon.domain.model.Coupon;
import com.mercadolibre.coupon.domain.model.Product;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Comparator;

@Component
public class RemoveProductLowestPriceFilter implements ProductFilter<Coupon> {

    @Override
    public Mono<Coupon> applyFilter(final Mono<Coupon> coupon) {
        return coupon.flatMap(this::filterRemoveProductLowestPrice);
    }

    // Private Method
    private Mono<Coupon> filterRemoveProductLowestPrice(final Coupon coupon) {
        if (coupon.getApplyFilter()) {
            final var productsToRedeem = coupon.getProductsRedeemable().isEmpty()
                    ? new ArrayList<Product>(coupon.getProducts())
                    : new ArrayList<Product>(coupon.getProductsRedeemable());

            productsToRedeem.sort(Comparator.comparingInt(Product::getPriceConversion));
            productsToRedeem.remove(0);

            final var sumTotalRemoveProductLowestPrice =
                    productsToRedeem.stream().mapToInt(Product::getPriceConversion).sum();

            if (coupon.getAmountConversion().intValue() >= sumTotalRemoveProductLowestPrice) {
                coupon.setApplyFilter(Boolean.FALSE);
                coupon.setProductsRedeemable(productsToRedeem);
                coupon.setAmountRedeemable(productsToRedeem.stream().mapToDouble(Product::getPrice).sum());
            }
        }

        return Mono.just(coupon);
    }

}

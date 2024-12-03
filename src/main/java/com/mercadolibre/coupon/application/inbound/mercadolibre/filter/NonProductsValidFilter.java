package com.mercadolibre.coupon.application.inbound.mercadolibre.filter;

import com.mercadolibre.coupon.application.inbound.mercadolibre.ProductFilter;
import com.mercadolibre.coupon.domain.model.Coupon;
import com.mercadolibre.coupon.domain.model.Product;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class NonProductsValidFilter implements ProductFilter<Coupon> {

    @Override
    public Mono<Coupon> applyFilter(final Mono<Coupon> coupon) {
        return coupon.flatMap(this::filterNonProductsValid);
    }

    // Private Method
    private Mono<Coupon> filterNonProductsValid(final Coupon coupon) {
        if (coupon.getApplyFilter()) {
            var amountRedeemed = new AtomicReference<Double>(0.00d);
            final var filteredProducts = new ArrayList<Product>();

            final var productsToRedeem = coupon.getProductsRedeemable().isEmpty()
                    ? coupon.getProducts().stream().toList()
                    : coupon.getProductsRedeemable().stream().toList();

            productsToRedeem
                    .stream()
                    .filter(product -> product.getPriceConversion() > 0)
                    .filter(product -> product.getPriceConversion() <= coupon.getAmountConversion())
                    .sorted(Comparator.comparingInt(Product::getPriceConversion))
                    .forEach(product -> {
                        filteredProducts.add(product);
                        amountRedeemed.updateAndGet(v -> Double.valueOf(v + product.getPrice()));
                    });

            if (filteredProducts.isEmpty()) {
                coupon.setApplyFilter(Boolean.FALSE);
            } else {
                coupon.setProductsRedeemable(filteredProducts);
                coupon.setAmountRedeemable(amountRedeemed.get());
            }
        }

        return Mono.just(coupon);
    }

}

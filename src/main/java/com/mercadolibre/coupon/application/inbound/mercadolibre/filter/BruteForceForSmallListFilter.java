package com.mercadolibre.coupon.application.inbound.mercadolibre.filter;

import com.mercadolibre.coupon.application.inbound.mercadolibre.ProductFilter;
import com.mercadolibre.coupon.domain.model.Coupon;
import com.mercadolibre.coupon.domain.model.Product;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Component
public class BruteForceForSmallListFilter implements ProductFilter<Coupon> {

    @Override
    public Mono<Coupon> applyFilter(final Mono<Coupon> coupon) {
        return coupon.flatMap(this::filterBruteForceForSmallList);
    }

    // Private Method
    private Mono<Coupon> filterBruteForceForSmallList(final Coupon coupon) {
        if (coupon.getApplyFilter() && coupon.getProductsRedeemable().size() <= 10) {
            // Initial values
            final var couponAmount = coupon.getAmountConversion().intValue();
            final var productsToRedeem = coupon.getProductsRedeemable().stream().toList();

            // Calculate best combination
            var bestCombination = this.calculateBestCombination(couponAmount, productsToRedeem);

            // Update info coupon
            coupon.setApplyFilter(Boolean.FALSE);
            coupon.setProductsRedeemable(bestCombination);
            coupon.setAmountRedeemable(bestCombination.stream().mapToDouble(Product::getPrice).sum());
        }

        return Mono.just(coupon);
    }

    private List<Product> calculateBestCombination(final Integer couponAmount, final List<Product> productsToRedeem) {
        int maxUsage = 0;
        final var totalProducts = productsToRedeem.size();
        final var bestCombination = new ArrayList<Product>();

        // Execution brute force for the calculate best combination
        for (int i = 0; i < (1 << totalProducts); i++) {
            int currentTotal = 0;
            final var currentCombination = new ArrayList<Product>();

            for (int j = 0; j < totalProducts; j++) {
                if ((i & (1 << j)) != 0) {
                    currentCombination.add(productsToRedeem.get(j));
                    currentTotal += productsToRedeem.get(j).getPriceConversion();
                }
            }

            if (currentTotal <= couponAmount && currentTotal > maxUsage) {
                maxUsage = currentTotal;
                bestCombination.clear();
                bestCombination.addAll(currentCombination);
            }
        }

        return bestCombination;
    }

}

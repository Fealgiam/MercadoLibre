package com.mercadolibre.coupon.application.inbound.mercadolibre.filter;

import com.mercadolibre.coupon.application.inbound.mercadolibre.ProductFilter;
import com.mercadolibre.coupon.domain.model.Coupon;
import com.mercadolibre.coupon.domain.model.Product;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class GreedyBruteForceForBigListFilter implements ProductFilter<Coupon> {

    @Override
    public Mono<Coupon> applyFilter(final Mono<Coupon> coupon) {
        return coupon.flatMap(this::filterBruteForceForSmallList);
    }

    // Private Method
    private Mono<Coupon> filterBruteForceForSmallList(final Coupon coupon) {
        if (coupon.getApplyFilter() && coupon.getProductsRedeemable().size() > 10) {
            // Initial values
            final var couponAmount = coupon.getAmountConversion().intValue();
            final var productsToRedeem = coupon.getProductsRedeemable().stream().toList();

            // Get list with big prices in products
            final var bigProducts = this.calculateBiggerPrices(couponAmount, productsToRedeem);

            // Get list with small prices in products
            final var smallProducts = this.calculateSmallerPrices(couponAmount, productsToRedeem);

            // Verify if is possible apply bruteforce algorithm
            final var bestCombination = calculateBestCombination(couponAmount, bigProducts, smallProducts);

            // Update info coupon
            coupon.setApplyFilter(Boolean.FALSE);
            coupon.setProductsRedeemable(bestCombination);
            coupon.setAmountRedeemable(bestCombination.stream().mapToDouble(Product::getPrice).sum());
        }

        return Mono.just(coupon);
    }

    private List<Product> calculateBiggerPrices(final Integer couponAmount, final List<Product> productsToRedeem) {
        Double sum = 0.0;
        final var biggerPrices = new ArrayList<Product>();
        final var productsDesc = new ArrayList<Product>(productsToRedeem);
        productsDesc.sort(Collections.reverseOrder());

        for (Product product : productsDesc) {
            if (sum + product.getPrice() <= couponAmount) {
                biggerPrices.add(product);
                sum += product.getPrice();
            }
        }

        return biggerPrices;
    }

    private List<Product> calculateSmallerPrices(final Integer couponAmount, final List<Product> productsToRedeem) {
        Double sum = 0.0;
        final var smallerPrices = new ArrayList<Product>();
        final var productsAsc = new ArrayList<Product>(productsToRedeem);

        for (Product product : productsAsc) {
            if (sum + product.getPrice() <= couponAmount) {
                smallerPrices.add(product);
                sum += product.getPrice();
            }
        }

        return smallerPrices;
    }

    private List<Product> calculateBestCombination(final Integer couponAmount,
                                                   final List<Product> bigProducts,
                                                   final List<Product> smallProducts) {
        final var bestCombination = new ArrayList<Product>();

        if (bigProducts.size() + smallProducts.size() <= 10) {
            var combination = new ArrayList<Product>();
            combination.addAll(bigProducts);
            combination.addAll(smallProducts);
            bestCombination.addAll(this.calculateBestCombinationBruteForce(couponAmount, combination));
        } else {
            double sumBigger = bigProducts.stream().mapToDouble(Product::getPrice).sum();
            double sumSmaller = smallProducts.stream().mapToDouble(Product::getPrice).sum();
            bestCombination.addAll(sumBigger > sumSmaller ? bigProducts : smallProducts);
        }

        return bestCombination;
    }

    private List<Product> calculateBestCombinationBruteForce(final Integer couponAmount,
                                                             final List<Product> originProducts) {
        int maxUsage = 0;
        final var totalProducts = originProducts.size();
        final var bestCombination = new ArrayList<Product>();

        // Execution brute force for the calculate best combination
        for (int i = 0; i < (1 << totalProducts); i++) {
            int currentTotal = 0;
            final var currentCombination = new ArrayList<Product>();

            for (int j = 0; j < totalProducts; j++) {
                if ((i & (1 << j)) != 0) {
                    currentCombination.add(originProducts.get(j));
                    currentTotal += originProducts.get(j).getPriceConversion();
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

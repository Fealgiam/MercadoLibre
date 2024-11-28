package com.mercadolibre.coupon.application.inbound.mercadolibre.step;

import com.mercadolibre.coupon.crosscutting.exception.business.BusinessException;
import com.mercadolibre.coupon.crosscutting.utility.PropagationExceptionUtility;
import com.mercadolibre.coupon.domain.context.MessageContext;
import com.mercadolibre.coupon.domain.context.MessageContextCoupon;
import com.mercadolibre.coupon.domain.model.Coupon;
import com.mercadolibre.coupon.domain.model.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.UnaryOperator;

import static com.mercadolibre.coupon.crosscutting.constant.MessageKeys.MSJ_BUSINESS_COUPON_INAPPLICABLE;
import static com.mercadolibre.coupon.crosscutting.constant.MessageKeys.MSJ_GEN_FOR_SUM_ERROR;
import static com.mercadolibre.coupon.crosscutting.utility.MessageUtility.getMessage;
import static com.mercadolibre.coupon.domain.context.MessageContextCoupon.COUPON;
import static com.mercadolibre.coupon.domain.context.MessageContextCoupon.PRODUCTS;
import static com.mercadolibre.coupon.domain.context.MessageContextCoupon.PRODUCTS_BY_COUPON;
import static java.lang.String.format;

@Log4j2
@Component
@RequiredArgsConstructor
public class CalculateProductsByCoupon implements UnaryOperator<MessageContext<MessageContextCoupon, Object>> {

    private static final String CLASS_NAME = CalculateProductsByCoupon.class.getSimpleName();

    @Value("${application.conversion-value}")
    private Integer conversionValue;


    @Override
    public MessageContext<MessageContextCoupon, Object>  apply(
            final MessageContext<MessageContextCoupon, Object> context) {
        try {
            // load properties
            final var coupon = context.getItem(COUPON, Coupon.class);

            final var amountCoupon = coupon.getAmount();
            final var bestOfferByCountry = new HashMap<String, Coupon>();
            @SuppressWarnings("unchecked")
            final Map<String, Set<Product>> productsByCountry = context.getItem(PRODUCTS, Map.class);

            // Calculate best offer for the coupon
            productsByCountry.entrySet().forEach(products -> {
                final var key = products.getKey();
                final var productsList = new ArrayList<>(products.getValue());
                final var couponRedeemed = this.maximizeCoupon(productsList, amountCoupon);

                if (couponRedeemed.getAmountRedeemable() > 0d) {
                    bestOfferByCountry.put(key, couponRedeemed);
                }
            });

            // Evaluate if coupon is redeemed
            if (bestOfferByCountry.isEmpty()) {
                throw new BusinessException(getMessage(MSJ_BUSINESS_COUPON_INAPPLICABLE));
            }

            // Add context
            context.addItem(PRODUCTS_BY_COUPON, bestOfferByCountry);

            return context;
        } catch (Exception ex) {
            log.error(format(getMessage(MSJ_GEN_FOR_SUM_ERROR), CLASS_NAME, "apply"));
            throw PropagationExceptionUtility.generateMercadoLibreException(ex);
        }
    }

    // Private Methods
    private Coupon maximizeCoupon(final List<Product> products, final double amountValue) {
        int intCouponValue = (int) Math.round(amountValue * conversionValue);
        int[] prices = products.stream().mapToInt(p -> (int) Math.round(p.getPrice() * conversionValue)).toArray();

        int n = prices.length;
        int[] dp = new int[intCouponValue + 1];
        boolean[][] selected = new boolean[n][intCouponValue + 1];

        for (int i = 0; i < n; i++) {
            for (int j = intCouponValue; j >= prices[i]; j--) {
                if (dp[j] < dp[j - prices[i]] + prices[i]) {
                    dp[j] = dp[j - prices[i]] + prices[i];
                    selected[i][j] = true;
                }
            }
        }

        Set<Product> includedProducts = new HashSet<>();
        for (int i = n - 1, j = intCouponValue; i >= 0; i--) {
            if (selected[i][j]) {
                includedProducts.add(products.get(i));
                j -= prices[i];
            }
        }

        return Coupon
                .builder()
                .amount(amountValue)
                .amountRedeemable(Double.valueOf(dp[intCouponValue] / conversionValue))
                .products(new HashSet<>(products))
                .productsRedeemable(includedProducts)
                .build();
    }

}
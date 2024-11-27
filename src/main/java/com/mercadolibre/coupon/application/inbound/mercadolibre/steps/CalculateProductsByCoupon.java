package com.mercadolibre.coupon.application.inbound.mercadolibre.steps;

import com.mercadolibre.coupon.application.outbound.ProductService;
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

import static com.mercadolibre.coupon.crosscutting.constants.MessageKeys.MSJ_GEN_FOR_SUM_ERROR;
import static com.mercadolibre.coupon.crosscutting.utility.MessageUtility.getMessage;
import static com.mercadolibre.coupon.domain.context.MessageContextCoupon.COUPON;
import static com.mercadolibre.coupon.domain.context.MessageContextCoupon.PRODUCTS;
import static com.mercadolibre.coupon.domain.context.MessageContextCoupon.PRODUCTS_BY_COUPON;
import static java.lang.String.format;

@Log4j2
@Component
@RequiredArgsConstructor
public class CalculateProductsByCoupon implements UnaryOperator<MessageContext<Enum<MessageContextCoupon>, Object>> {

    private static final String CLASS_NAME = CalculateProductsByCoupon.class.getSimpleName();

    @Value("${application.conversion-value}")
    private Integer conversionValue;


    @Override
    public MessageContext<Enum<MessageContextCoupon>, Object> apply(
            final MessageContext<Enum<MessageContextCoupon>, Object> context) {
        try {
            // load properties
            final var coupon = context.getItem(COUPON, Coupon.class);

            final var amountCoupon = coupon.getAmount();
            final Map<String, Coupon> couponMap = new HashMap<>();
            final Map<String, Set<Product>> productsByCountry = context.getItem(PRODUCTS, Map.class);

            //
            productsByCountry.entrySet().forEach(products -> {
                final var key = products.getKey();
                final var productsList = new ArrayList<>(products.getValue());
                couponMap.put(key, this.maximizeCoupon(productsList, amountCoupon));
            });


            context.addItem(PRODUCTS_BY_COUPON, couponMap);

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
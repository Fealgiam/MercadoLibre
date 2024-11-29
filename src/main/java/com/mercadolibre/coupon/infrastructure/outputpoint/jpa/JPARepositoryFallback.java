package com.mercadolibre.coupon.infrastructure.outputpoint.jpa;

import com.mercadolibre.coupon.application.outbound.CouponOutPort;
import com.mercadolibre.coupon.application.outbound.ProductRedeemedOutPort;
import com.mercadolibre.coupon.crosscutting.utility.PropagationExceptionUtility;
import com.mercadolibre.coupon.domain.model.Coupon;
import com.mercadolibre.coupon.domain.model.Product;
import lombok.extern.log4j.Log4j2;

import java.util.Set;

@Log4j2
public abstract class JPARepositoryFallback implements CouponOutPort, ProductRedeemedOutPort {

    // Fallback methods
    protected void saveCouponRedeemedFallback(final Coupon coupon, final Throwable throwable) {
        log.debug("saveCouponRedeemedFallback");
        throw PropagationExceptionUtility.generateMercadoLibreException(throwable);
    }

    protected Set<Product> fetchProductRedeemedFallback(final Integer limit, final Throwable throwable) {
        log.debug("fetchProductRedeemedFallback");
        throw PropagationExceptionUtility.generateMercadoLibreException(throwable);
    }

    protected Set<Product> fetchProductRedeemedByCountryFallback(final String country,
                                                                 final Integer limit,
                                                                 final Throwable throwable) {
        log.debug("fetchProductRedeemedByCountryFallback");
        throw PropagationExceptionUtility.generateMercadoLibreException(throwable);
    }

}

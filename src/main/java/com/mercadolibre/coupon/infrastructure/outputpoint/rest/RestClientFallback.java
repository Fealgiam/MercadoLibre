package com.mercadolibre.coupon.infrastructure.outputpoint.rest;

import com.mercadolibre.coupon.application.outbound.CountryOutPort;
import com.mercadolibre.coupon.application.outbound.ProductOutPort;
import com.mercadolibre.coupon.crosscutting.utility.PropagationExceptionUtility;
import com.mercadolibre.coupon.domain.model.Country;
import com.mercadolibre.coupon.domain.model.Product;
import lombok.extern.log4j.Log4j2;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Log4j2
public abstract class RestClientFallback implements CountryOutPort, ProductOutPort {

    // Fallback methods
    private void commonFallbackMethod(Throwable throwable) {
        log.debug("commonFallbackMethod");
        throw PropagationExceptionUtility.generateMercadoLibreException(throwable);
    }

    protected Optional<Country> fetchCountryFallback(final String countryCode, final Throwable throwable) {
        commonFallbackMethod(throwable);
        return Optional.empty();
    }

    protected Set<Country> fetchCountriesFallback(final Throwable throwable) {
        this.commonFallbackMethod(throwable);
        return Set.of();
    }

    protected Optional<Product> fetchProductFallback(final String productId, final Throwable throwable) {
        this.commonFallbackMethod(throwable);
        return Optional.empty();
    }

    protected Set<Product> fetchProductsFallback(final Collection<String> productIds, final Throwable throwable) {
        this.commonFallbackMethod(throwable);
        return Set.of();
    }

}

package com.mercadolibre.coupon.application.outbound;

import com.mercadolibre.coupon.domain.model.Country;

import java.util.Optional;
import java.util.Set;

public interface CountryService {

    Optional<Country> fetchCountry(final String countryCode);

    Set<Country> fetchCountries();

}

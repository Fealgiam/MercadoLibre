package com.mercadolibre.coupon.application.outbound;

import com.mercadolibre.coupon.domain.model.Country;

import java.util.List;
import java.util.Optional;

public interface CountryOutPort {

    Optional<Country> fetchCountry(final String countryCode);

    List<Country> fetchCountries();

}

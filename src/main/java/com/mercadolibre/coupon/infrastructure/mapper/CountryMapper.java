package com.mercadolibre.coupon.infrastructure.mapper;

import com.mercadolibre.coupon.domain.model.Country;
import com.mercadolibre.coupon.infrastructure.model.outputpoint.rest.CountryRs;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
@NoArgsConstructor
public class CountryMapper {

    public Country mapper(final String countryCode) {
        return Optional
                .ofNullable(countryCode)
                .map(country -> Country.builder().code(country).build())
                .orElse(Country.builder().build());
    }

    public Country mapper(final CountryRs countryRs) {
        return Optional
                .ofNullable(countryRs)
                .map(country -> Country
                        .builder()
                        .code(country.getId())
                        .currency(country.getCurrency())
                        .name(country.getName())
                        .build())
                .orElse(Country.builder().build());
    }

    public List<Country> mapper(final Set<CountryRs> countryRs) {
        return Optional
                .ofNullable(countryRs)
                .orElse(Set.of())
                .stream()
                .map(this::mapper)
                .toList();
    }

}

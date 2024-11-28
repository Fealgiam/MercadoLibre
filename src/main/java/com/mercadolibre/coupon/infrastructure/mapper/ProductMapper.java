package com.mercadolibre.coupon.infrastructure.mapper;

import com.mercadolibre.coupon.domain.model.Product;
import com.mercadolibre.coupon.infrastructure.model.outputpoint.rest.ProductRs;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProductMapper {

    private final CountryMapper countryMapper;


    public Product mapper(final String productId) {
        return Optional
                .ofNullable(productId)
                .map(p -> Product.builder().id(p).build())
                .orElse(Product.builder().build());
    }

    public Set<Product> mapperFromIds(final Set<String> productIds) {
        return Optional
                .ofNullable(productIds)
                .orElse(Set.of())
                .stream()
                .map(this::mapper)
                .collect(Collectors.toSet());
    }

    public Product mapper(final ProductRs productRs) {
        return Optional
                .ofNullable(productRs)
                .map(p -> Product
                        .builder()
                        .id(p.getId())
                        .price(p.getPrice())
                        .country(countryMapper.mapper(p.getCountryCode()))
                        .build())
                .orElse(Product.builder().build());
    }

    public Set<Product> mapper(final Set<ProductRs> productsRs) {
        return Optional
                .ofNullable(productsRs)
                .orElse(Set.of())
                .stream()
                .map(this::mapper)
                .collect(Collectors.toSet());
    }

}

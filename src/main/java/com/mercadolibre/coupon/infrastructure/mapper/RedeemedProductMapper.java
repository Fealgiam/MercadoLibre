package com.mercadolibre.coupon.infrastructure.mapper;

import com.mercadolibre.coupon.domain.model.Product;
import com.mercadolibre.coupon.infrastructure.model.outputpoint.entity.RedeemedProduct;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@NoArgsConstructor
public class RedeemedProductMapper {

    public RedeemedProduct mapper(final Product product) {
        return Optional
                .ofNullable(product)
                .map(p -> RedeemedProduct
                        .builder()
                        .idProduct(p.getId())
                        .idCountry(p.getCountry().getCode())
                        .build())
                .orElse(RedeemedProduct.builder().build());
    }

    public Set<RedeemedProduct> mapper(final Set<Product> products) {
        return Optional
                .ofNullable(products)
                .orElse(Set.of())
                .stream()
                .map(this::mapper)
                .collect(Collectors.toSet());
    }

}

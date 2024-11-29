package com.mercadolibre.coupon.infrastructure.mapper;

import com.mercadolibre.coupon.domain.model.Product;
import com.mercadolibre.coupon.infrastructure.model.outputpoint.entity.RedeemedProductDAO;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@NoArgsConstructor
public class RedeemedProductDAOMapper {

    public RedeemedProductDAO mapper(final Product product) {
        return Optional
                .ofNullable(product)
                .map(p -> RedeemedProductDAO
                        .builder()
                        .idProduct(p.getId())
                        .idCountry(p.getCountry().getCode())
                        .build())
                .orElse(RedeemedProductDAO.builder().build());
    }

    public Set<RedeemedProductDAO> mapper(final Set<Product> products) {
        return Optional
                .ofNullable(products)
                .orElse(Set.of())
                .stream()
                .map(this::mapper)
                .collect(Collectors.toSet());
    }

}

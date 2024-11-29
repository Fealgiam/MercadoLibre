package com.mercadolibre.coupon.infrastructure.mapper;

import com.mercadolibre.coupon.domain.model.Product;
import com.mercadolibre.coupon.infrastructure.model.entrypoint.Product.v1.ProductRsV1;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@NoArgsConstructor
public class ProductRsV1Mapper {

    public ProductRsV1 mapper(Set<Product> products) {
        return Optional
                .ofNullable(products)
                .map(productRedeemed -> ProductRsV1
                        .builder()
                        .itemsRedeemed(getMapFromSetProducts(productRedeemed))
                        .build())
                .orElse(ProductRsV1.builder().build());
    }

    // Private Methods
    private Map<String, Long> getMapFromSetProducts(Set<Product> products) {
        return products.stream().collect(Collectors.toMap(Product::getId, Product::getRedeemed));
    }

}

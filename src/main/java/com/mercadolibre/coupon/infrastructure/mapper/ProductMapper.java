package com.mercadolibre.coupon.infrastructure.mapper;

import com.mercadolibre.coupon.domain.model.Product;
import com.mercadolibre.coupon.infrastructure.model.outputpoint.entity.RedeemedProductDAO;
import com.mercadolibre.coupon.infrastructure.model.outputpoint.rest.ProductRs;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ProductMapper {

    @Value("${application.conversion-value}")
    private Integer conversionValue;

    private final CountryMapper countryMapper;


    public Product mapper(final String productId) {
        return Optional
                .ofNullable(productId)
                .map(p -> Product.builder().id(p).build())
                .orElse(Product.builder().build());
    }

    public List<Product> mapperFromIds(final Set<String> productIds) {
        return Optional
                .ofNullable(productIds)
                .orElse(Set.of())
                .stream()
                .map(this::mapper)
                .toList();
    }

    public Product mapper(final ProductRs productRs) {
        return Optional
                .ofNullable(productRs)
                .map(p -> Product
                        .builder()
                        .id(p.getId())
                        .price(p.getPrice())
                        .priceConversion((int) (p.getPrice() * conversionValue))
                        .country(countryMapper.mapper(p.getCountryCode()))
                        .build())
                .orElse(Product.builder().build());
    }

    public List<Product> mapper(final Set<ProductRs> productsRs) {
        return Optional
                .ofNullable(productsRs)
                .orElse(Set.of())
                .stream()
                .map(this::mapper)
                .toList();
    }

    public Product mapper(final RedeemedProductDAO productDao) {
        return Optional
                .ofNullable(productDao)
                .map(pDao -> Product
                        .builder()
                        .country(countryMapper.mapper(pDao.getIdCountry()))
                        .id(pDao.getIdProduct())
                        .timesRedeemed(pDao.getNumberRedeemed())
                        .build())
                .orElse(Product.builder().build());
    }

    public List<Product> mapperDao(final List<RedeemedProductDAO> productDaos) {
        return Optional
                .ofNullable(productDaos)
                .orElse(List.of())
                .stream()
                .map(this::mapper)
                .toList();
    }

}

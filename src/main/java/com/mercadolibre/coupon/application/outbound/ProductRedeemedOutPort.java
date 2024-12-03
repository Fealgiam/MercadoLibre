package com.mercadolibre.coupon.application.outbound;

import com.mercadolibre.coupon.domain.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRedeemedOutPort {

    List<Product> fetchProductRedeemed(final Optional<Integer> customPaging);

    List<Product> fetchProductRedeemedByCountry(final String country, final Optional<Integer> customPaging);

}

package com.mercadolibre.coupon.application.inbound;

import com.mercadolibre.coupon.domain.model.Product;

import java.util.Optional;
import java.util.Set;

public interface ProductInPort {

    Set<Product> fetchRedeemedProducts(final Optional<Integer> maxRecords);

    Set<Product> fetchRedeemedProductsByCountry(final String country, final Optional<Integer> maxRecords);

}

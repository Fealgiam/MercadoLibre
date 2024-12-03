package com.mercadolibre.coupon.application.inbound;

import com.mercadolibre.coupon.domain.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductInPort {

    List<Product> fetchRedeemedProducts(final Optional<Integer> maxRecords);

    List<Product> fetchRedeemedProductsByCountry(final String country, final Optional<Integer> maxRecords);

}

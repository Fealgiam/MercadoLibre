package com.mercadolibre.coupon.application.outbound;

import com.mercadolibre.coupon.domain.model.Product;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ProductOutPort {

    Optional<Product> fetchProduct(final String productId);

    List<Product> fetchProducts(final Collection<String> productIds);

}

package com.mercadolibre.coupon.application.outbound;

import com.mercadolibre.coupon.domain.model.Product;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface ProductOutPort {

    Optional<Product> fetchProduct(final String productId);

    Set<Product> fetchProducts(final Collection<String> productIds);

}

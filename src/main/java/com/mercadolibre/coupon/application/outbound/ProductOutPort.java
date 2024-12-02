package com.mercadolibre.coupon.application.outbound;

import com.mercadolibre.coupon.domain.model.Product;

import java.util.Collection;
import java.util.Optional;

public interface ProductOutPort {

    Optional<Product> fetchProduct(final String productId);

    Collection<Product> fetchProducts(final Collection<String> productIds);

}

package com.mercadolibre.coupon.application.outbound;

import com.mercadolibre.coupon.domain.model.Product;

import java.util.Set;

public interface ProductRedeemedOutPort {

    Set<Product> fetchProductRedeemed(final Integer customPaging);

    Set<Product> fetchProductRedeemedByCountry(final String country, final Integer customPaging);

}

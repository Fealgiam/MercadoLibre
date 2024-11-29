package com.mercadolibre.coupon.infrastructure.entrypoint.rest;

import com.mercadolibre.coupon.infrastructure.model.entrypoint.DataResponse;
import com.mercadolibre.coupon.infrastructure.model.entrypoint.Product.ProductRqRs;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static com.mercadolibre.coupon.crosscutting.constant.ResourceEndpoints.X_API_VERSION;

public interface CouponProductsController<O extends ProductRqRs> {

    default ResponseEntity<DataResponse<O>> buildResponse(final O response) {
        var responseHeaders = new HttpHeaders();
        responseHeaders.set(X_API_VERSION, response.getVersion());

        return ResponseEntity
                .ok()
                .headers(responseHeaders)
                .body(DataResponse.<O>builder().data(response).build());
    }

    ResponseEntity<DataResponse<O>> fetchTopProductsRedeemedByCoupon(final Optional<Integer> numRecords);

    ResponseEntity<DataResponse<O>> fetchTopProductsRedeemedByCouponByCountry(final String countryCode,
                                                                              final Optional<Integer> numRecords);

}

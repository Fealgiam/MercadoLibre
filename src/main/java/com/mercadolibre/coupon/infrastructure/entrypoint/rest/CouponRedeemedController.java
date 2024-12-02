package com.mercadolibre.coupon.infrastructure.entrypoint.rest;

import com.mercadolibre.coupon.infrastructure.model.entrypoint.DataResponse;
import com.mercadolibre.coupon.infrastructure.model.entrypoint.coupon.CouponRqRs;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import static com.mercadolibre.coupon.crosscutting.constant.ResourceEndpoints.X_API_VERSION;

public interface CouponRedeemedController<I extends CouponRqRs, O extends CouponRqRs> {

    // Common methods
    default ResponseEntity<DataResponse<O>> buildResponse(final O response) {
        var responseHeaders = new HttpHeaders();
        responseHeaders.set(X_API_VERSION, response.getVersion());

        return ResponseEntity
                .ok()
                .headers(responseHeaders)
                .body(DataResponse.<O>builder().data(response).build());
    }

    // Contract methods
    ResponseEntity<DataResponse<O>> calculateBestOfferCoupon(final I coupons);

}

package com.mercadolibre.coupon.infrastructure.entrypoint.rest;

import com.mercadolibre.coupon.infrastructure.model.entrypoint.DataResponse;
import com.mercadolibre.coupon.infrastructure.model.entrypoint.coupon.CouponRqRs;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.mercadolibre.coupon.crosscutting.constant.ResourceEndpoints.COUPON_PATH;
import static com.mercadolibre.coupon.crosscutting.constant.ResourceEndpoints.X_API_VERSION;

@RequestMapping(path = COUPON_PATH)
public interface MercadoLibreCouponController<I extends CouponRqRs, O extends CouponRqRs> {

    default ResponseEntity<DataResponse<O>> buildResponse(O response) {
        var responseHeaders = new HttpHeaders();
        responseHeaders.set(X_API_VERSION, response.getVersion());

        return ResponseEntity
                .ok()
                .headers(responseHeaders)
                .body(DataResponse.<O>builder().data(response).build());
    }

    ResponseEntity<DataResponse<O>> calculateBestOfferCoupon(I coupons);

}

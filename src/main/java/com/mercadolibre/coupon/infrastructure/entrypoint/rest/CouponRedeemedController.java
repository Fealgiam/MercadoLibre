package com.mercadolibre.coupon.infrastructure.entrypoint.rest;

import com.mercadolibre.coupon.infrastructure.model.entrypoint.DataResponse;
import com.mercadolibre.coupon.infrastructure.model.entrypoint.coupon.CouponRqRs;
import com.mercadolibre.coupon.infrastructure.model.entrypoint.coupon.v1.CouponRqV1;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(
            summary = "Calculate the best redeemable coupon",
            description = "Calculates the best redeemable coupon based on the request provided")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200",
                            content = {
                                    @Content(schema = @Schema(implementation = DataResponse.class), mediaType = "application/json")
                            }),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Some parameter required is not present in the request.",
                            content = {
                                    @Content(schema = @Schema(implementation = DataResponse.class), mediaType = "application/json")
                            }),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Indicate what an error occur and is not possible respond the request.",
                            content = {
                                    @Content(schema = @Schema(implementation = DataResponse.class), mediaType = "application/json")
                            })
            })
    ResponseEntity<DataResponse<O>> calculateBestOfferCoupon(
            @RequestBody(
                    description = "Contains the value of the coupon and the list of identifiers of the products to be redeemed.",
                    required = true,
                    content = @Content(schema = @Schema(implementation = DataResponse.class))) final I coupons);

}

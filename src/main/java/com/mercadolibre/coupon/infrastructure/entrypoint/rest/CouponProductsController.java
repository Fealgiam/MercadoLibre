package com.mercadolibre.coupon.infrastructure.entrypoint.rest;

import com.mercadolibre.coupon.infrastructure.model.entrypoint.DataResponse;
import com.mercadolibre.coupon.infrastructure.model.entrypoint.Product.ProductRqRs;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static com.mercadolibre.coupon.crosscutting.constant.ResourceEndpoints.X_API_VERSION;

public interface CouponProductsController<O extends ProductRqRs> {

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
            summary = "You get the most redeemed products with coupons",
            description = "You get the most redeemed products with coupons, it does not take into account any filters")
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
    ResponseEntity<DataResponse<O>> fetchTopProductsRedeemedByCoupon(
            @Parameter(description = "Number of records expected in response.")
            final Optional<Integer> numRecords);

    @Operation(
            summary = "You get the most redeemed products with coupons by country",
            description = "You get the most redeemed products with coupons, taking into account the indicated country")
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
                            responseCode = "404",
                            description = "The country not exist or is disabled in the moment.",
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
    ResponseEntity<DataResponse<O>> fetchTopProductsRedeemedByCouponByCountry(
            @Parameter(description = "The country code by which you want to obtain the products exchanged for coupons.")
            final String countryCode,
            @Parameter(description = "Number of records expected in response.")
            final Optional<Integer> numRecords);

}

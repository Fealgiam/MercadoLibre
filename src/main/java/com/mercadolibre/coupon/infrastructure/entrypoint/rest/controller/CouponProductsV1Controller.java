package com.mercadolibre.coupon.infrastructure.entrypoint.rest.controller;

import com.mercadolibre.coupon.application.inbound.ProductInPort;
import com.mercadolibre.coupon.crosscutting.utility.PropagationExceptionUtility;
import com.mercadolibre.coupon.infrastructure.entrypoint.rest.CouponProductsController;
import com.mercadolibre.coupon.infrastructure.mapper.ProductRsV1Mapper;
import com.mercadolibre.coupon.infrastructure.model.entrypoint.DataResponse;
import com.mercadolibre.coupon.infrastructure.model.entrypoint.Product.v1.ProductRsV1;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static com.mercadolibre.coupon.crosscutting.constant.MessageKeys.MSJ_GEN_FOR_SUM_ERROR;
import static com.mercadolibre.coupon.crosscutting.constant.ResourceEndpoints.COUNTRY_CODE_PARAM;
import static com.mercadolibre.coupon.crosscutting.constant.ResourceEndpoints.COUPON_PRODUCTS_COUNTRY_PATH;
import static com.mercadolibre.coupon.crosscutting.constant.ResourceEndpoints.COUPON_PRODUCTS_PATH;
import static com.mercadolibre.coupon.crosscutting.constant.ResourceEndpoints.NUMBER_RECORD_PARAM;
import static com.mercadolibre.coupon.crosscutting.constant.ResourceEndpoints.X_API_VERSION_V1;
import static com.mercadolibre.coupon.crosscutting.utility.MessageUtility.getMessage;
import static java.lang.String.format;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping(path = COUPON_PRODUCTS_PATH, headers = {X_API_VERSION_V1})
@Tag(name = "CouponProductsV1Controller", description = "Controller to consult products redeemed for coupons. Version #1")
public class CouponProductsV1Controller implements CouponProductsController<ProductRsV1> {

    private static final String CLASS_NAME = CouponProductsV1Controller.class.getSimpleName();

    // Mappers
    private final ProductRsV1Mapper productRsV1Mapper;

    // Services
    private final ProductInPort productService;


    @Override
    @Retry(name = "couponProducts")
    @RateLimiter(name = "couponProducts")
    @CircuitBreaker(name = "couponProducts")
    @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DataResponse<ProductRsV1>> fetchTopProductsRedeemedByCoupon(
            @RequestParam(value = NUMBER_RECORD_PARAM, required = false) final Optional<Integer> limit) {
        try {
            var products = productService.fetchRedeemedProducts(limit);
            return this.buildResponse(productRsV1Mapper.mapper(products));
        } catch (Exception ex) {
            log.error(format(getMessage(MSJ_GEN_FOR_SUM_ERROR), CLASS_NAME, "fetchTopProductsRedeemedByCoupon"));
            throw PropagationExceptionUtility.generateMercadoLibreException(ex);
        }
    }

    @Override
    @Retry(name = "couponProducts")
    @RateLimiter(name = "couponProducts")
    @CircuitBreaker(name = "couponProducts")
    @GetMapping(
            value = COUPON_PRODUCTS_COUNTRY_PATH,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DataResponse<ProductRsV1>> fetchTopProductsRedeemedByCouponByCountry(
            @PathVariable(value = COUNTRY_CODE_PARAM) final String countryCode,
            @RequestParam(value = NUMBER_RECORD_PARAM, required = false) final Optional<Integer> limit) {
        try {
            var products = productService.fetchRedeemedProductsByCountry(countryCode, limit);
            return this.buildResponse(productRsV1Mapper.mapper(products));
        } catch (Exception ex) {
            log.error(format(getMessage(MSJ_GEN_FOR_SUM_ERROR), CLASS_NAME, "fetchTopProductsRedeemedByCouponByCountry"));
            throw PropagationExceptionUtility.generateMercadoLibreException(ex);
        }
    }

}

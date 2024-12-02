package com.mercadolibre.coupon.infrastructure.entrypoint.rest.controller;

import com.mercadolibre.coupon.application.inbound.CouponInPort;
import com.mercadolibre.coupon.crosscutting.utility.PropagationExceptionUtility;
import com.mercadolibre.coupon.domain.model.Coupon;
import com.mercadolibre.coupon.infrastructure.entrypoint.rest.CouponRedeemedController;
import com.mercadolibre.coupon.infrastructure.mapper.CouponMapper;
import com.mercadolibre.coupon.infrastructure.mapper.CouponRsV1Mapper;
import com.mercadolibre.coupon.infrastructure.model.entrypoint.DataResponse;
import com.mercadolibre.coupon.infrastructure.model.entrypoint.coupon.v1.CouponRqV1;
import com.mercadolibre.coupon.infrastructure.model.entrypoint.coupon.v1.CouponRsV1;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.mercadolibre.coupon.crosscutting.constant.MessageKeys.MSJ_GEN_FOR_SUM_ERROR;
import static com.mercadolibre.coupon.crosscutting.constant.ResourceEndpoints.COUPON_PATH;
import static com.mercadolibre.coupon.crosscutting.constant.ResourceEndpoints.X_API_VERSION_V1;
import static com.mercadolibre.coupon.crosscutting.utility.MessageUtility.getMessage;
import static java.lang.String.format;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping(path = COUPON_PATH, headers = {X_API_VERSION_V1})
public class CouponRedeemedV1Controller implements CouponRedeemedController<CouponRqV1, CouponRsV1> {

    private static final String CLASS_NAME = CouponRedeemedV1Controller.class.getSimpleName();

    // Mappers
    private final CouponMapper couponMapper;
    private final CouponRsV1Mapper couponRsV1Mapper;

    // Services
    private final CouponInPort couponService;


    @Override
    @Retry(name = "couponRedeemed")
    @RateLimiter(name = "couponRedeemed")
    @CircuitBreaker(name = "couponRedeemed")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DataResponse<CouponRsV1>> calculateBestOfferCoupon(@Valid @RequestBody final CouponRqV1 coupons) {
        try {
            Coupon couponRedeemable = couponService.calculateBestOfferCoupon(couponMapper.mapper(coupons));
            return this.buildResponse(couponRsV1Mapper.mapper(couponRedeemable));
        } catch (Exception ex) {
            log.error(format(getMessage(MSJ_GEN_FOR_SUM_ERROR), CLASS_NAME, "calculateBestOfferCoupon"));
            throw PropagationExceptionUtility.generateMercadoLibreException(ex);
        }
    }

}

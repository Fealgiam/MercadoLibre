package com.mercadolibre.coupon.infrastructure.entrypoint.rest.controller;

import com.mercadolibre.coupon.application.inbound.CouponService;
import com.mercadolibre.coupon.application.inbound.mercadolibre.CouponIterativeService;
import com.mercadolibre.coupon.crosscutting.utility.PropagationExceptionUtility;
import com.mercadolibre.coupon.infrastructure.entrypoint.rest.MercadoLibreCouponController;
import com.mercadolibre.coupon.infrastructure.mapper.CouponMapper;
import com.mercadolibre.coupon.infrastructure.model.entrypoint.DataResponse;
import com.mercadolibre.coupon.infrastructure.model.entrypoint.coupon.v1.CouponV1Rq;
import com.mercadolibre.coupon.infrastructure.model.entrypoint.coupon.v1.CouponV1Rs;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.mercadolibre.coupon.crosscutting.constants.MessageKeys.MSJ_GEN_FOR_SUM_ERROR;
import static com.mercadolibre.coupon.crosscutting.constants.ResourceEndpoints.COUPON_PATH;
import static com.mercadolibre.coupon.crosscutting.constants.ResourceEndpoints.X_API_VERSION_V1;
import static com.mercadolibre.coupon.crosscutting.utility.MessageUtility.getMessage;
import static java.lang.String.format;

/**
 * Class in charge of capturing requests related to coupons
 *
 * @author Alejandro Gil Amaya
 * @version v1
 */
@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping(path = COUPON_PATH, headers = {X_API_VERSION_V1})
public class MercadoLibreCouponV1Controller implements MercadoLibreCouponController<CouponV1Rq, CouponV1Rs> {

    private static final String CLASS_NAME = MercadoLibreCouponV1Controller.class.getSimpleName();

    // Mappers
    private final CouponMapper couponMapper;

    // Services
    private final CouponService couponIterativeService;


    @Override
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DataResponse<CouponV1Rs>> calculateProductsCoupon(@Valid @RequestBody CouponV1Rq coupons) {
        try {
            couponIterativeService.calculateProductsCoupon(couponMapper.mapper(coupons));
            return this.buildResponse(CouponV1Rs.builder().build());
        } catch (Exception ex) {
            log.error(format(getMessage(MSJ_GEN_FOR_SUM_ERROR), CLASS_NAME, "calculateProductsCoupon"));
            throw PropagationExceptionUtility.generateMercadoLibreException(ex);
        }
    }

}

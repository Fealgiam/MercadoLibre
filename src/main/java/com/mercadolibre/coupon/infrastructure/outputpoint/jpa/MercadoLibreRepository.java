package com.mercadolibre.coupon.infrastructure.outputpoint.jpa;

import com.mercadolibre.coupon.application.outbound.CouponProductService;
import com.mercadolibre.coupon.crosscutting.exception.technical.TechnicalException;
import com.mercadolibre.coupon.crosscutting.utility.PropagationExceptionUtility;
import com.mercadolibre.coupon.domain.model.Coupon;
import com.mercadolibre.coupon.infrastructure.mapper.RedeemedProductMapper;
import com.mercadolibre.coupon.infrastructure.outputpoint.jpa.repository.RedeemedProductRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.mercadolibre.coupon.crosscutting.constant.MessageKeys.MSJ_GEN_FOR_SUM_ERROR;
import static com.mercadolibre.coupon.crosscutting.utility.MessageUtility.getMessage;
import static java.lang.String.format;

@Log4j2
@Component
@RequiredArgsConstructor
public class MercadoLibreRepository implements CouponProductService {

    private static final String CLASS_NAME = MercadoLibreRepository.class.getSimpleName();

    // Mappers
    private final RedeemedProductMapper redeemedProductMapper;

    // Repositories
    private final RedeemedProductRepository redeemedProductRepository;


    // Custom methods
    @Override
    @Transactional
    @CircuitBreaker(name = "repositoryCouponClient", fallbackMethod = "saveCouponRedeemedFallback")
    public void saveCouponRedeemed(final Coupon coupon) {
        try {
            final var ids = redeemedProductMapper.mapper(coupon.getProductsRedeemable());
            redeemedProductRepository.saveAll(ids);
        } catch (Exception ex) {
            log.error(format(getMessage(MSJ_GEN_FOR_SUM_ERROR), CLASS_NAME, "saveCouponRedeemed"));
            throw new TechnicalException(ex.getMessage(), ex);
        }
    }

    // Fallback methods
    public void saveCouponRedeemedFallback(final Coupon coupon, final Throwable throwable) {
        log.debug("saveCouponRedeemedFallback");
        throw PropagationExceptionUtility.generateMercadoLibreException(throwable);
    }

}

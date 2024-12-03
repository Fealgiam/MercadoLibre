package com.mercadolibre.coupon.infrastructure.outputpoint.jpa;

import com.mercadolibre.coupon.application.outbound.CouponOutPort;
import com.mercadolibre.coupon.application.outbound.ProductRedeemedOutPort;
import com.mercadolibre.coupon.crosscutting.exception.technical.TechnicalException;
import com.mercadolibre.coupon.domain.model.Coupon;
import com.mercadolibre.coupon.domain.model.Product;
import com.mercadolibre.coupon.infrastructure.mapper.ProductMapper;
import com.mercadolibre.coupon.infrastructure.mapper.RedeemedProductDAOMapper;
import com.mercadolibre.coupon.infrastructure.outputpoint.jpa.repository.RedeemedProductRepository;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.mercadolibre.coupon.crosscutting.constant.MessageKeys.MSJ_GEN_FOR_SUM_ERROR;
import static com.mercadolibre.coupon.crosscutting.utility.MessageUtility.getMessage;
import static java.lang.String.format;

@Log4j2
@Component
@RequiredArgsConstructor
public class JPARepository implements CouponOutPort, ProductRedeemedOutPort {

    private static final String CLASS_NAME = JPARepository.class.getSimpleName();

    @Value("${application.default-limit}")
    private Integer defaultLimit;

    // Mappers
    private final ProductMapper productMapper;
    private final RedeemedProductDAOMapper redeemedProductDAOMapper;

    // Repositories
    private final RedeemedProductRepository redeemedProductRepository;


    // Custom methods
    @Override
    @Retry(name = "repositoryCouponClient")
    @Transactional
    public void saveCouponRedeemed(final Coupon coupon) {
        try {
            final var ids = redeemedProductDAOMapper.mapper(coupon.getProductsRedeemable());
            redeemedProductRepository.saveAll(ids);
        } catch (Exception ex) {
            log.error(format(getMessage(MSJ_GEN_FOR_SUM_ERROR), CLASS_NAME, "saveCouponRedeemed"));
            throw new TechnicalException(ex.getMessage(), ex);
        }
    }

    @Override
    @Retry(name = "repositoryCouponClient")
    public List<Product> fetchProductRedeemed(final Optional<Integer> limit) {
        try {
            final Pageable pageable = PageRequest.of(0, limit.orElse(defaultLimit), Sort.by("numberRedeemed"));
            final var productsRedeemedTop = redeemedProductRepository.findAllByOrderByNumberRedeemedDesc(pageable);

            return productMapper.mapperDao(productsRedeemedTop.getContent());
        } catch (Exception ex) {
            log.error(format(getMessage(MSJ_GEN_FOR_SUM_ERROR), CLASS_NAME, "fetchProductRedeemed"));
            throw new TechnicalException(ex.getMessage(), ex);
        }
    }

    @Override
    @Retry(name = "repositoryCouponClient")
    public List<Product> fetchProductRedeemedByCountry(final String country, final Optional<Integer> limit) {
        try {
            final Pageable pageable = PageRequest.of(0, limit.orElse(defaultLimit), Sort.by("numberRedeemed"));

            final var productsRedeemedTop = redeemedProductRepository
                    .findByIdCountryOrderByNumberRedeemedDesc(country, pageable);

            return productMapper.mapperDao(productsRedeemedTop.getContent());
        } catch (Exception ex) {
            log.error(format(getMessage(MSJ_GEN_FOR_SUM_ERROR), CLASS_NAME, "fetchProductRedeemedByCountry"));
            throw new TechnicalException(ex.getMessage(), ex);
        }
    }

}

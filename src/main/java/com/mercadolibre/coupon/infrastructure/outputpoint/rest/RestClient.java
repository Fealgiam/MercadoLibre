package com.mercadolibre.coupon.infrastructure.outputpoint.rest;

import com.mercadolibre.coupon.crosscutting.exception.technical.TechnicalException;
import com.mercadolibre.coupon.crosscutting.utility.PropagationExceptionUtility;
import com.mercadolibre.coupon.domain.model.Country;
import com.mercadolibre.coupon.domain.model.Product;
import com.mercadolibre.coupon.infrastructure.mapper.CountryMapper;
import com.mercadolibre.coupon.infrastructure.mapper.ProductMapper;
import com.mercadolibre.coupon.infrastructure.model.outputpoint.rest.ProductRs;
import com.mercadolibre.coupon.infrastructure.outputpoint.rest.client.MercadoLibreFeignClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static com.mercadolibre.coupon.crosscutting.constant.Constants.CACHE_CALL_APIS;
import static com.mercadolibre.coupon.crosscutting.constant.Constants.NAME_CACHE_CALL_API_COUNTRIES;
import static com.mercadolibre.coupon.crosscutting.constant.Constants.NAME_CACHE_CALL_API_COUNTRY;
import static com.mercadolibre.coupon.crosscutting.constant.MessageKeys.MSJ_GEN_FOR_SUM_ERROR;
import static com.mercadolibre.coupon.crosscutting.utility.MessageUtility.getMessage;
import static java.lang.String.format;

@Log4j2
@Component
@RequiredArgsConstructor
public class RestClient extends RestClientFallback {

    private static final String CLASS_NAME = RestClient.class.getSimpleName();

    // Manager Cache
    private final CacheManager cacheManager;

    // Mappers
    private final CountryMapper countryMapper;
    private final ProductMapper productMapper;

    // Services
    private final MercadoLibreFeignClient mercadoLibreFeignClient;


    // Clean cache
    @Scheduled(cron = "${cron-task.cache.api-call-refresh}")
    public void clearCacheCountry(){
        CACHE_CALL_APIS.forEach( cacheName -> {
            if(Objects.nonNull(cacheManager.getCache(cacheName))) {
                cacheManager.getCache(cacheName).clear();
            }
        });
    }

    // Custom methods
    @Override
    @Cacheable(value = NAME_CACHE_CALL_API_COUNTRY, cacheManager = "mercadoLibreCacheManager")
    @CircuitBreaker(name = "feignCountryClient", fallbackMethod = "fetchCountryFallback")
    public Optional<Country> fetchCountry(final String countryCode) {
        try {
            final var countryRs = mercadoLibreFeignClient.getSite(countryCode);
            return Optional.ofNullable(countryMapper.mapper(countryRs));
        } catch (Exception ex) {
            log.error(format(getMessage(MSJ_GEN_FOR_SUM_ERROR), CLASS_NAME, "fetchCountry"));
            throw new TechnicalException(ex.getMessage(), ex);
        }
    }

    @Override
    @Cacheable(value = NAME_CACHE_CALL_API_COUNTRIES, cacheManager = "mercadoLibreCacheManager")
    @CircuitBreaker(name = "feignCountryClient", fallbackMethod = "fetchCountriesFallback")
    public Set<Country> fetchCountries() {
        try {
            final var countryRs = mercadoLibreFeignClient.getSites();
            return countryMapper.mapper(countryRs);
        } catch (Exception ex) {
            log.error(format(getMessage(MSJ_GEN_FOR_SUM_ERROR), CLASS_NAME, "fetchCountries"));
            throw new TechnicalException(ex.getMessage(), ex);
        }
    }

    @Override
    @CircuitBreaker(name = "feignProductClient", fallbackMethod = "fetchProductFallback")
    public Optional<Product> fetchProduct(final String productId) {
        try {
            final var productRs = mercadoLibreFeignClient.getItem(productId);
            return Optional.ofNullable(productMapper.mapper(productRs));
        } catch (Exception ex) {
            log.error(format(getMessage(MSJ_GEN_FOR_SUM_ERROR), CLASS_NAME, "fetchProduct"));
            throw new TechnicalException(ex.getMessage(), ex);
        }
    }

    @Override
    @CircuitBreaker(name = "feignProductClient", fallbackMethod = "fetchProductsFallback")
    public Set<Product> fetchProducts(final Collection<String> productIds) {
        try {
            // TODO - Implement call services to get multiple products. - The service requires authentication.
            //final var ids = String.join(",", productIds);
            //final var productsRs = mercadoLibreFeignClient.getItems(ids);

            final var productsRs = new HashSet<ProductRs>();
            productIds.forEach(productId -> productsRs.add(mercadoLibreFeignClient.getItem(productId)));

            return productMapper.mapper(productsRs);
        } catch (Exception ex) {
            log.error(format(getMessage(MSJ_GEN_FOR_SUM_ERROR), CLASS_NAME, "fetchProducts"));
            throw PropagationExceptionUtility.generateMercadoLibreException(ex);
        }
    }

}

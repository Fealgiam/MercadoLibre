package com.mercadolibre.coupon.infrastructure.outputpoint.rest;

import com.mercadolibre.coupon.application.outbound.CountryService;
import com.mercadolibre.coupon.application.outbound.ProductService;
import com.mercadolibre.coupon.crosscutting.exception.technicalexception.TechnicalException;
import com.mercadolibre.coupon.crosscutting.utility.PropagationExceptionUtility;
import com.mercadolibre.coupon.domain.model.Country;
import com.mercadolibre.coupon.domain.model.Product;
import com.mercadolibre.coupon.infrastructure.mapper.CountryMapper;
import com.mercadolibre.coupon.infrastructure.mapper.ProductMapper;
import com.mercadolibre.coupon.infrastructure.model.outputpoint.rest.CountryRs;
import com.mercadolibre.coupon.infrastructure.model.outputpoint.rest.ProductRs;
import com.mercadolibre.coupon.infrastructure.outputpoint.rest.client.MercadoLibreFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static com.mercadolibre.coupon.crosscutting.constants.MessageKeys.MSJ_GEN_FOR_SUM_ERROR;
import static com.mercadolibre.coupon.crosscutting.utility.MessageUtility.getMessage;
import static java.lang.String.format;

@Log4j2
@Component
@RequiredArgsConstructor
public class MercadoLibreRestClient implements CountryService, ProductService {

    private static final String CLASS_NAME = MercadoLibreRestClient.class.getSimpleName();

    // Mappers
    private final CountryMapper countryMapper;
    private final ProductMapper productMapper;

    // Services
    private final MercadoLibreFeignClient mercadoLibreFeignClient;


    @Override
    public Optional<Country> fetchCountry(final String countryCode) {
        try {
            final CountryRs countryRs = mercadoLibreFeignClient.getSite(countryCode);

            return Optional.ofNullable(countryMapper.mapper(countryRs));
        } catch (Exception ex) {
            log.error(format(getMessage(MSJ_GEN_FOR_SUM_ERROR), CLASS_NAME, "fetchCountry", ex.getMessage()));
            throw new TechnicalException(ex.getMessage(), ex);
        }
    }

    @Override
    public Set<Country> fetchCountries() {
        try {
            final Set<CountryRs> countryRs = mercadoLibreFeignClient.getSites();

            return countryMapper.mapper(countryRs);
        } catch (Exception ex) {
            log.error(format(getMessage(MSJ_GEN_FOR_SUM_ERROR), CLASS_NAME, "fetchCountries", ex.getMessage()));
            throw new TechnicalException(ex.getMessage(), ex);
        }
    }

    @Override
    public Optional<Product> fetchProduct(final String productId) {
        try {
            final ProductRs productRs = mercadoLibreFeignClient.getItem(productId);

            return Optional.ofNullable(productMapper.mapper(productRs));
        } catch (Exception ex) {
            log.error(format(getMessage(MSJ_GEN_FOR_SUM_ERROR), CLASS_NAME, "fetchProduct", ex.getMessage()));
            throw new TechnicalException(ex.getMessage(), ex);
        }
    }

    @Override
    public Set<Product> fetchProducts(final Collection<String> productIds) {
        try {
            //final String ids = String.join(",", productIds);
            //final Set<ProductRs> productsRs = mercadoLibreFeignClient.getItems(ids);

            Set<ProductRs> productsRs = new HashSet<>();
            productIds.forEach(productId -> {
                productsRs.add(mercadoLibreFeignClient.getItem(productId));
            });

            return productMapper.mapper(productsRs);
        } catch (Exception ex) {
            log.error(format(getMessage(MSJ_GEN_FOR_SUM_ERROR), CLASS_NAME, "fetchProducts"));
            throw PropagationExceptionUtility.generateMercadoLibreException(ex);
        }
    }

}

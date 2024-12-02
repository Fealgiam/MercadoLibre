package com.mercadolibre.coupon.configuration;

import com.mercadolibre.coupon.crosscutting.exception.MercadoLibreException;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Predicate;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Configuration
public class ResilienceConfiguration {

    @Bean
    public CircuitBreakerRegistry circuitBreakerRegistry() {
        Predicate<Throwable> exceptionPredicate = throwable ->
                (throwable instanceof MercadoLibreException ex)
                        ? (ex.getHttpStatus() == BAD_REQUEST || ex.getHttpStatus() == UNPROCESSABLE_ENTITY)
                        : false;

        CircuitBreakerConfig config = CircuitBreakerConfig
                .custom()
                .ignoreException(exceptionPredicate)
                .build();

        return CircuitBreakerRegistry.of(config);
    }

    @Bean
    public RetryRegistry retryRegistry() {
        Predicate<Throwable> exceptionPredicate = throwable ->
                (throwable instanceof MercadoLibreException ex)
                        ? (ex.getHttpStatus() != BAD_REQUEST && ex.getHttpStatus() != UNPROCESSABLE_ENTITY)
                        : true;

        RetryConfig config = RetryConfig.custom()
                .retryOnException(exceptionPredicate)
                .build();

        return RetryRegistry.of(config);
    }

}

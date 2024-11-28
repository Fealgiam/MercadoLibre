package com.mercadolibre.coupon.configuration;

import com.mercadolibre.coupon.crosscutting.exception.MercadoLibreException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Objects;

import static com.mercadolibre.coupon.crosscutting.constant.MessageKeys.MSJ_EX_CODE_TECHNICAL;
import static com.mercadolibre.coupon.crosscutting.constant.MessageKeys.MSJ_RQ_VAL_HEADER_LANGUAGE;
import static com.mercadolibre.coupon.crosscutting.constant.MessageKeys.MSJ_RQ_VAL_HEADER_VERSION;
import static com.mercadolibre.coupon.crosscutting.constant.ResourceEndpoints.X_API_VERSION;
import static com.mercadolibre.coupon.crosscutting.utility.MessageUtility.getMessage;

@Log4j2
@Configuration
public class WebConfiguration implements WebMvcConfigurer, HandlerInterceptor {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (Objects.isNull(request.getHeader(X_API_VERSION))) {
            throw new MercadoLibreException(
                    getMessage(MSJ_EX_CODE_TECHNICAL), getMessage(MSJ_RQ_VAL_HEADER_VERSION), HttpStatus.BAD_REQUEST);
        }

        if (Objects.isNull(request.getHeader(HttpHeaders.ACCEPT_LANGUAGE))) {
            log.warn(getMessage(MSJ_RQ_VAL_HEADER_LANGUAGE));
        }

        return true;
    }

}

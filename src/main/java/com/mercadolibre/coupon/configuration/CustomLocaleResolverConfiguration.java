package com.mercadolibre.coupon.configuration;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Configuration
public class CustomLocaleResolverConfiguration extends AcceptHeaderLocaleResolver {

    private static final String DEFAULT_LOCALE = "es";

    private static final List<Locale> LOCALES = Arrays
            .asList(Locale.forLanguageTag("es"), Locale.forLanguageTag("en"));


    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        Locale localeResult;

        if (StringUtils.isEmpty(request.getHeader(HttpHeaders.ACCEPT_LANGUAGE))) {
            localeResult = Locale.forLanguageTag(DEFAULT_LOCALE);
        } else {
            var list = Locale.LanguageRange.parse(request.getHeader(HttpHeaders.ACCEPT_LANGUAGE));
            localeResult = Locale.lookup(list, LOCALES);
        }

        return localeResult;
    }

    @Bean
    public LocaleResolver localeResolver() {
        return new CustomLocaleResolverConfiguration();
    }

}

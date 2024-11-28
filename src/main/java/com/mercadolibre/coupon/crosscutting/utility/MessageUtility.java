package com.mercadolibre.coupon.crosscutting.utility;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.nio.charset.StandardCharsets;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageUtility {

    private static final String DEFAULTS_PATH = "classpath:i18n/mercado_libre_message";
    private static final String DEFAULT_BUNDLE_PATH = "classpath:i18n/mercado_libre_message";

    private static ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource;

    static {
        reloadableResourceBundleMessageSource = new ReloadableResourceBundleMessageSource();

        reloadableResourceBundleMessageSource.setBasenames(DEFAULT_BUNDLE_PATH, DEFAULTS_PATH);
        reloadableResourceBundleMessageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
    }

    public static String getMessage(final String key, final String... params) {
        return reloadableResourceBundleMessageSource.getMessage(key, params, LocaleContextHolder.getLocale());
    }

}

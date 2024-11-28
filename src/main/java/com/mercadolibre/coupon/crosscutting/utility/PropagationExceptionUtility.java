package com.mercadolibre.coupon.crosscutting.utility;

import com.mercadolibre.coupon.crosscutting.exception.MercadoLibreException;
import com.mercadolibre.coupon.crosscutting.exception.technical.TechnicalException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PropagationExceptionUtility {

    public static MercadoLibreException generateMercadoLibreException(Exception ex) {
        return (ex instanceof MercadoLibreException exception)
                ? exception
                : new TechnicalException(ex.getMessage(), ex);
    }

    public static MercadoLibreException generateMercadoLibreException(Throwable ex) {
        return (ex instanceof MercadoLibreException exception)
                ? exception
                : new TechnicalException(ex.getMessage(), ex);
    }

}

package com.mercadolibre.coupon.crosscutting.utility;

import com.mercadolibre.coupon.crosscutting.exception.MercadoLibreException;
import com.mercadolibre.coupon.crosscutting.exception.technicalexception.TechnicalException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PropagationExceptionUtility {

    public static MercadoLibreException generateMercadoLibreException(Exception ex) {
        return (ex instanceof MercadoLibreException exception)
                ? exception
                : new TechnicalException(ex.getMessage(), ex);
    }

}

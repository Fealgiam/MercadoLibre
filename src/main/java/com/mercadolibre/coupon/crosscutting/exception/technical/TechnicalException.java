package com.mercadolibre.coupon.crosscutting.exception.technical;

import com.mercadolibre.coupon.crosscutting.exception.MercadoLibreException;
import com.mercadolibre.coupon.crosscutting.utility.MessageUtility;
import org.springframework.http.HttpStatus;

import java.io.Serial;

import static com.mercadolibre.coupon.crosscutting.constant.MessageKeys.MSJ_EX_CODE_TECHNICAL;

public class TechnicalException extends MercadoLibreException {

    @Serial
    private static final long serialVersionUID = -7206580683336805348L;


    public TechnicalException(String message) {
        super(MessageUtility.getMessage(MSJ_EX_CODE_TECHNICAL), message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public TechnicalException(String message, Throwable cause) {
        super(MessageUtility.getMessage(MSJ_EX_CODE_TECHNICAL), message, HttpStatus.INTERNAL_SERVER_ERROR, cause);
    }

}

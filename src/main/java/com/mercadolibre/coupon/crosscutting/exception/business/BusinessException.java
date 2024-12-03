package com.mercadolibre.coupon.crosscutting.exception.business;

import com.mercadolibre.coupon.crosscutting.exception.MercadoLibreException;
import com.mercadolibre.coupon.crosscutting.utility.MessageUtility;
import org.springframework.http.HttpStatus;

import java.io.Serial;

import static com.mercadolibre.coupon.crosscutting.constant.MessageKeys.MSJ_EX_CODE_BUSINESS;

public class BusinessException extends MercadoLibreException {

    @Serial
    private static final long serialVersionUID = 6659103242562665705L;


    public BusinessException(String message) {
        super(MessageUtility.getMessage(MSJ_EX_CODE_BUSINESS), message, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    public BusinessException(String message, Throwable cause) {
        super(MessageUtility.getMessage(MSJ_EX_CODE_BUSINESS), message, HttpStatus.UNPROCESSABLE_ENTITY, cause);
    }

}

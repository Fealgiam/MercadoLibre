package com.mercadolibre.coupon.crosscutting.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.io.Serial;

@Getter
@Setter(AccessLevel.PROTECTED)
public class MercadoLibreException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 6122488072862015523L;

    protected String code;
    protected HttpStatus httpStatus;


    public MercadoLibreException(String code, String message, HttpStatus httpStatus) {
        super(message);
        this.code = code;
        this.httpStatus = httpStatus;
    }

    public MercadoLibreException(String code, String message, HttpStatus httpStatus, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.httpStatus = httpStatus;
    }

}

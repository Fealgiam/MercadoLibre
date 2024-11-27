package com.mercadolibre.coupon.infrastructure.entrypoint.rest;

import com.mercadolibre.coupon.crosscutting.exception.MercadoLibreException;
import com.mercadolibre.coupon.crosscutting.exception.technicalexception.TechnicalException;
import com.mercadolibre.coupon.infrastructure.model.entrypoint.DataResponse;
import com.mercadolibre.coupon.infrastructure.model.entrypoint.ErrorResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import static com.mercadolibre.coupon.crosscutting.constants.MessageKeys.MSJ_GEN_TECHNICAL_ERROR;
import static com.mercadolibre.coupon.crosscutting.utility.MessageUtility.getMessage;
import static java.lang.String.format;

@Log4j2
@ControllerAdvice
public class MercadoLibreExceptionHandler {

    // Private Methods
    private ResponseEntity<DataResponse<?>> buildResponse(WebRequest request,
                                                          Object messageError,
                                                          HttpStatus httpStatus) {
        var errorResponse = ErrorResponse
                .builder()
                .code(httpStatus.value())
                .status(httpStatus.getReasonPhrase())
                .message(messageError)
                .path(((ServletWebRequest) request).getRequest().getRequestURI().toString())
                .build();

        return new ResponseEntity<>(DataResponse.builder().errorResponse(errorResponse).build(), httpStatus);
    }

    // Custom Methods
    @ExceptionHandler(MercadoLibreException.class)
    public ResponseEntity<DataResponse<?>> technicalException(WebRequest request, MercadoLibreException exception) {
        var httpStatus = exception.getHttpStatus();
        var messageError = exception instanceof TechnicalException
                ? getMessage(MSJ_GEN_TECHNICAL_ERROR)
                : exception.getMessage();

        log.error(exception.getMessage(), exception);
        return this.buildResponse(request, messageError, httpStatus);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<DataResponse<?>> exception(WebRequest request, MethodArgumentNotValidException exception) {
        var messageError = exception
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> format("%s:%s", fieldError.getField(), getMessage(fieldError.getDefaultMessage())))
                .toList();

        log.error(exception.getMessage(), exception);
        return this.buildResponse(request, messageError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<DataResponse<?>> exception(WebRequest request, Exception exception) {
        log.error(exception.getMessage(), exception);
        return this.buildResponse(request, getMessage(MSJ_GEN_TECHNICAL_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}

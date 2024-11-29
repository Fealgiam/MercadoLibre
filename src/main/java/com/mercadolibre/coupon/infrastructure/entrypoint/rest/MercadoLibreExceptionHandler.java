package com.mercadolibre.coupon.infrastructure.entrypoint.rest;

import com.mercadolibre.coupon.crosscutting.exception.MercadoLibreException;
import com.mercadolibre.coupon.crosscutting.exception.technical.TechnicalException;
import com.mercadolibre.coupon.infrastructure.model.entrypoint.DataResponse;
import com.mercadolibre.coupon.infrastructure.model.entrypoint.ErrorResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import static com.mercadolibre.coupon.crosscutting.constant.MessageKeys.MSJ_GEN_FOR_ERROR;
import static com.mercadolibre.coupon.crosscutting.constant.MessageKeys.MSJ_GEN_TECHNICAL_ERROR;
import static com.mercadolibre.coupon.crosscutting.constant.MessageKeys.MSJ_RQ_VAL_HEADER_CONTENT_TYPE;
import static com.mercadolibre.coupon.crosscutting.utility.MessageUtility.getMessage;
import static java.lang.String.format;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Log4j2
@ControllerAdvice
public class MercadoLibreExceptionHandler {

    private static final String CLASS_NAME = MercadoLibreExceptionHandler.class.getSimpleName();

    // Private Methods
    private ResponseEntity<DataResponse<?>> buildResponse(WebRequest request,
                                                          Object messageError,
                                                          HttpStatus httpStatus,
                                                          Exception exception) {
        var errorResponse = ErrorResponse
                .builder()
                .code(httpStatus.value())
                .customCode(exception instanceof MercadoLibreException ex ? ex.getCode() : null)
                .status(httpStatus.getReasonPhrase())
                .message(messageError)
                .path(((ServletWebRequest) request).getRequest().getRequestURI().toString())
                .build();

        log.error(format(getMessage(MSJ_GEN_FOR_ERROR), CLASS_NAME, "buildResponse"), exception);
        return new ResponseEntity<>(DataResponse.builder().errorResponse(errorResponse).build(), httpStatus);
    }

    // Custom Methods
    @ExceptionHandler(MercadoLibreException.class)
    public ResponseEntity<DataResponse<?>> exception(WebRequest request, MercadoLibreException exception) {
        var httpStatus = exception.getHttpStatus();

        var messageError = exception instanceof TechnicalException
                ? getMessage(MSJ_GEN_TECHNICAL_ERROR)
                : exception.getMessage();

        return this.buildResponse(request, messageError, httpStatus, exception);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<DataResponse<?>> exception(WebRequest request, MethodArgumentNotValidException exception) {
        var messageError = exception
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> format("%s:%s", fieldError.getField(), getMessage(fieldError.getDefaultMessage())))
                .toList();

        return this.buildResponse(request, messageError, BAD_REQUEST, exception);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<DataResponse<?>> exception(WebRequest request, HttpMediaTypeNotSupportedException exception) {
        return this.buildResponse(request, getMessage(MSJ_RQ_VAL_HEADER_CONTENT_TYPE), BAD_REQUEST, exception);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<DataResponse<?>> exception(WebRequest request, NoResourceFoundException exception) {
        return this.buildResponse(request, getMessage(MSJ_GEN_TECHNICAL_ERROR), NOT_FOUND, exception);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<DataResponse<?>> exception(WebRequest request, Exception exception) {
        return this.buildResponse(request, getMessage(MSJ_GEN_TECHNICAL_ERROR), INTERNAL_SERVER_ERROR, exception);
    }

}

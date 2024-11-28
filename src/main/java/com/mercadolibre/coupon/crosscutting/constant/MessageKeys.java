package com.mercadolibre.coupon.crosscutting.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageKeys {

    /**
     * Messages generic
     */
    public static final String MSJ_GEN_FOR_ERROR = "message.generic.format.error";
    public static final String MSJ_GEN_FOR_SUM_ERROR = "message.generic.format.error.summary";
    public static final String MSJ_GEN_TECHNICAL_ERROR = "message.generic.technical.error";

    /**
     * Messages exceptions
     */
    public static final String MSJ_EX_CODE_BUSINESS = "message.exception.code.business";
    public static final String MSJ_EX_CODE_TECHNICAL = "message.exception.code.technical";

    /**
     * Messages request validation
     */
    public static final String MSJ_RQ_VAL_AMOUNT_NULL = "message.request.validation.amount.null";
    public static final String MSJ_RQ_VAL_AMOUNT_POSITIVE = "message.request.validation.amount.positive";

    public static final String MSJ_RQ_VAL_ITEMS_NULL = "message.request.validation.items.null";
    public static final String MSJ_RQ_VAL_ITEMS_EMPTY = "message.request.validation.items.empty";

    public static final String MSJ_RQ_VAL_HEADER_VERSION = "message.request.validation.header.version";
    public static final String MSJ_RQ_VAL_HEADER_LANGUAGE = "message.request.validation.header.language";

    /**
     * Messages context error
     */
    public static final String MSJ_CONTEXT_NULL = "message.context.null";
    public static final String MSJ_CONTEXT_CAST = "message.context.cast";

    /**
     * Messages business validation
     */
    public static final String MSJ_BUSINESS_COUPON_INAPPLICABLE = "message.business.coupon.inapplicable";

}

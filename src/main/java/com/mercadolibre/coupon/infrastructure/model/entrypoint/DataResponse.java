package com.mercadolibre.coupon.infrastructure.model.entrypoint;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataResponse<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 7880405194382161911L;

    private T data;

    @JsonProperty("page_response")
    private PageResponse pageResponse;

    @JsonProperty("error_response")
    private ErrorResponse errorResponse;

}